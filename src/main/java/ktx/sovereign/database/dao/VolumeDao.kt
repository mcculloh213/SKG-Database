package ktx.sovereign.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ktx.sovereign.database.entity.Content
import ktx.sovereign.database.entity.Volume

@Dao
abstract class VolumeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(vararg volume: Volume)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertContent(vararg content: Content)
    @Query("SELECT * FROM volumes")
    abstract fun getAll(): List<Volume>
    @Query("SELECT * FROM volumes WHERE _id = :id LIMIT 1")
    abstract fun get(id: String): Volume?
}