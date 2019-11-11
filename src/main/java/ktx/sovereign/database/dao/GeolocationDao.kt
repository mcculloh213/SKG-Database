package ktx.sovereign.database.dao

import androidx.room.Dao
import ktx.sovereign.database.entity.Geolocation

@Dao
abstract class GeolocationDao : BaseDao<Geolocation>() {

}