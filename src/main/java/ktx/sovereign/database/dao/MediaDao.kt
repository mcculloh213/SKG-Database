package ktx.sovereign.database.dao

import androidx.room.Dao
import ktx.sovereign.database.entity.Media

@Dao
abstract class MediaDao : BaseDao<Media>() {
}