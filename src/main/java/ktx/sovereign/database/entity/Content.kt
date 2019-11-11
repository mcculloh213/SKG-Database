package ktx.sovereign.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ktx.sovereign.api.model.ContentRestModel
import ktx.sovereign.database.SchemaInfo

@Parcelize
@Entity(tableName = SchemaInfo.Content.TableName)
data class Content(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SchemaInfo.Content.PrimaryKey) val id: Long,
    @ColumnInfo(name = SchemaInfo.Content.Title) val title: String,
    @ColumnInfo(name = SchemaInfo.Content.Path) val path: String,
    @ColumnInfo(name = SchemaInfo.Content.Volume) val token: String,
    @ColumnInfo(name = SchemaInfo.Content.Index) val index: Int,
    @ColumnInfo(name = SchemaInfo.Content.Checksum) val checksum: String
) : Parcelable {
    @Ignore
    constructor(title: String, token: String, index: Int, checksum: String) :
            this(0, title, "", token, index, checksum)
    companion object {
        fun from(page: ContentRestModel.VolumePage, token: String) = Content(
                id = 0,
                title = page.title,
                path = page.key,
                token = token,
                index = page.index,
                checksum = ""
        )
    }
}