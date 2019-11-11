package ktx.sovereign.database.entity

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ktx.sovereign.database.SchemaInfo

@Parcelize
@Entity(tableName = SchemaInfo.Image.TableName)
data class Image (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SchemaInfo.Image.PrimaryKey) var id: Long = 0,
    @ColumnInfo(name = SchemaInfo.Image.DisplayName) var display: String = "",
    @ColumnInfo(name = SchemaInfo.Image.Path) var path: String = ""
) : Parcelable {
    @Ignore
    constructor()
            : this(0, "", "")
    @Ignore
    constructor(display: String, path: String)
            : this(0, display, path)
}