package ktx.sovereign.database.callback

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ktx.sovereign.database.ApplicationDatabase
import ktx.sovereign.database.entity.Note
import java.util.*
import kotlin.coroutines.CoroutineContext

class SeedNotesCallback(
        private val context: Context
) : RoomDatabase.Callback(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun onOpen(db: SupportSQLiteDatabase) {
        launch {
            val now = Date()
            ApplicationDatabase.getDatabase(context)
                    .NoteDao().apply {
                        insert(Note(
                                1L,
                                "Title",
                                "Description",
                                false,
                                now.time,
                                now.time
                        ))
                        insert(Note(
                                2L,
                                "Maintenance Report",
                                "Successfully completed maintenance on robot.",
                                false,
                                now.time + (5 * 60 * 1_000),
                                now.time + (5 * 60 * 1_000)
                        ))
                        insert(Note(
                                3L,
                                "Unix Epoch",
                                "It has been ${Date().time} milliseconds since the Unix Epoch.",
                                true,
                                now.time + (10 * 60 * 1_000),
                                now.time + (10 * 60 * 1_000)
                        ))
                        insert(Note(
                                4L,
                                "Ideas",
                                "I'm running out of ideas to turn into notes.",
                                false,
                                now.time + (15 * 60 * 1_000),
                                now.time + (15 * 60 * 1_000)
                        ))
                        insert(Note(
                                5L,
                                "Imagine",
                                "Imagine there's no heaven\nIt's easy if you try\nNo hell below us\nAbove us only sky\n\n-John Lennon",
                                true,
                                now.time + (20 * 60 * 1_000),
                                now.time + (20 * 60 * 1_000)
                        ))
                    }
        }
    }
}