package ktx.sovereign.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ktx.sovereign.database.SchemaInfo

@Parcelize
@Entity(tableName = SchemaInfo.Geolocation.TableName)
data class Geolocation (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SchemaInfo.Geolocation.PrimaryKey) var id: Long,
    @ColumnInfo(name = SchemaInfo.Geolocation.Latitude) var latitude: Double,
    @ColumnInfo(name = SchemaInfo.Geolocation.Longitude) var longitude: Double,
    @ColumnInfo(name = SchemaInfo.Geolocation.Altitude) var altitude: Double,
    @ColumnInfo(name = SchemaInfo.Geolocation.Timestamp) val timestamp: Long,
    @ColumnInfo(name = SchemaInfo.Geolocation.Description) var description: String? = null
) : Parcelable {
    @Ignore
    constructor()
            : this(0, 0.0, 0.0, 0.0, -1, null)
    @Ignore
    constructor(latitude: Double, longitude: Double) :
            this(0, latitude, longitude, 0.0, -1, null)
    @Ignore
    constructor(latitude: Double, longitude: Double, altitude: Double, timestamp: Long, description: String? = null)
            : this(0, latitude, longitude, altitude, timestamp, description)
}