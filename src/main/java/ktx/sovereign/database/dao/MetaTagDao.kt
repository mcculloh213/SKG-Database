package ktx.sovereign.database.dao

import androidx.room.Dao
import ktx.sovereign.database.entity.MetaTag

@Dao
abstract class MetaTagDao : BaseDao<MetaTag>() {
}