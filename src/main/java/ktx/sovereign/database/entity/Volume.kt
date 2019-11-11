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
@Entity(tableName = SchemaInfo.Volume.TableName)
data class Volume(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = SchemaInfo.Volume.PrimaryKey) val id: String,
    @ColumnInfo(name = SchemaInfo.Volume.Name) var name: String,
    @ColumnInfo(name = SchemaInfo.Volume.Description) var description: String,
    @ColumnInfo(name = SchemaInfo.Volume.Token) var token: String,
    @ColumnInfo(name = SchemaInfo.Volume.IsCredential) val isCredential: Boolean = false
) : Parcelable {
    @Ignore
    constructor() :
            this("", "", "", "")
    companion object {
        fun from(response: ContentRestModel.Volume) = Volume (
                id = response.id,
                name = response.name,
                description =  response.description,
                token = response.token,
                isCredential = response.isCredential
        )
    }
}