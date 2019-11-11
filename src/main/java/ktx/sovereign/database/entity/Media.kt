package ktx.sovereign.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ktx.sovereign.database.SchemaInfo

@Parcelize
@Entity(tableName = SchemaInfo.Media.TableName)
data class Media(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SchemaInfo.Media.PrimaryKey) val id: Long,
    @ColumnInfo(name = SchemaInfo.Media.DateAdded) val added: Long,
    @ColumnInfo(name = SchemaInfo.Media.DateModified) var modified: Long,
    @ColumnInfo(name = SchemaInfo.Media.DisplayName) var display: String,
    @ColumnInfo(name = SchemaInfo.Media.Duration) val duration: Long,
    @ColumnInfo(name = SchemaInfo.Media.Height) val height: Int,
    @ColumnInfo(name = SchemaInfo.Media.MimeType) val mime: String,
    @ColumnInfo(name = SchemaInfo.Media.Orientation) var orientation: Int,
    @ColumnInfo(name = SchemaInfo.Media.Size) val size: Int,
    @ColumnInfo(name = SchemaInfo.Media.Title) var title: String,
    @ColumnInfo(name = SchemaInfo.Media.Width) val width: Int
) : Parcelable