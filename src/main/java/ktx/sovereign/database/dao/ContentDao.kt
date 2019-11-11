package ktx.sovereign.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ktx.sovereign.database.entity.Content

@Dao
abstract class ContentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(vararg content: Content)
    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun update(vararg content: Content): Int
    @Delete
    abstract fun delete(vararg content: Content): Int

    @Query("SELECT * FROM content")
    abstract fun getAll(): List<Content>
    @Query("SELECT * FROM content")
    abstract fun getAllContent(): LiveData<List<Content>>
    @Query("SELECT * FROM content WHERE content._token = :token")
    abstract fun getAllVolumeContent(token: String): List<Content>
    @Query("SELECT _checksum FROM content WHERE _id = :id")
    abstract fun readChecksum(id: String): String?
    @Query("SELECT * FROM content WHERE _id = :id LIMIT 1")
    abstract fun get(id: String): Content?
}