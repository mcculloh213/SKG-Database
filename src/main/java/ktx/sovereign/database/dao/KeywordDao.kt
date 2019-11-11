package ktx.sovereign.database.dao

import androidx.room.Dao
import ktx.sovereign.database.entity.Keyword

@Dao
abstract class KeywordDao : BaseDao<Keyword>() {
}