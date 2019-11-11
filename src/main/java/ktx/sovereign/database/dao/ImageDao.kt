package ktx.sovereign.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ktx.sovereign.database.entity.Image

@Dao
abstract class ImageDao : BaseDao<Image>() {
    @Query("SELECT * FROM images")
    abstract fun getAllImages(): LiveData<List<Image>>
}