@file:Suppress("FunctionName")

package ktx.sovereign.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ktx.sovereign.database.callback.SeedMenuCallback
import ktx.sovereign.database.dao.*
import ktx.sovereign.database.entity.*
import ktx.sovereign.database.fts.NoteFts
import ktx.sovereign.database.migration.Migration_1_2
import ktx.sovereign.database.migration.Migration_2_3
import ktx.sovereign.database.migration.Migration_3_4
import java.util.concurrent.Executors

@Database(
        entities = [
            Content::class,
            Document::class,
            Geolocation::class,
            Image::class,
            Keyword::class,
            Message::class,
            MetaTag::class,
            Note::class,
            NoteFts::class,
            NoteLocation::class,
            NoteMedia::class,
            NoteTag::class,
            ScrollingMenuItem::class,
            Volume::class
        ],
        exportSchema = true,
        version = 4
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun ContentDao(): ContentDao
    abstract fun DocumentDao(): DocumentDao
    abstract fun GeolocationDao(): GeolocationDao
    abstract fun ImageDao(): ImageDao
    abstract fun KeywordDao(): KeywordDao
    abstract fun MessageDao(): MessageDao
    abstract fun MetaTagDao(): MetaTagDao
    abstract fun NoteDao(): NoteDao
    abstract fun ScrollingMenuItemDao(): ScrollingMenuItemDao
    abstract fun VolumeDao(): VolumeDao

    companion object {
        @Volatile
        private var Instance: ApplicationDatabase? = null

        @JvmStatic fun getDatabase(context: Context): ApplicationDatabase {
            val temp = Instance
            if (temp != null) {
                return temp
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ApplicationDatabase::class.java,
                        "industrial_badger_app.db3")
                        .addCallback(SeedMenuCallback(context))
                        .addMigrations(
                                Migration_1_2(),
                                Migration_2_3(),
                                Migration_3_4()
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                Instance = instance
                return instance
            }
        }

        @JvmStatic private val DatabaseExecutor = Executors.newSingleThreadExecutor()
        @JvmStatic fun DatabaseThread(f: () -> Unit) = DatabaseExecutor.execute(f)
    }
}