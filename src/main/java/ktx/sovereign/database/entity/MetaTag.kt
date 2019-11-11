package ktx.sovereign.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ktx.sovereign.database.SchemaInfo

@Parcelize
@Entity(tableName = SchemaInfo.MetaTag.TableName)
data class MetaTag(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SchemaInfo.MetaTag.PrimaryKey) var id: Long,
    @ColumnInfo(name = SchemaInfo.MetaTag.Key) var key: String,
    @ColumnInfo(name = SchemaInfo.MetaTag.Value) var value: String
) : Parcelable {
    @Ignore
    constructor()
            : this(0, "", "")
    @Ignore
    constructor(key: String, value: String)
            : this(0, key, value)
}