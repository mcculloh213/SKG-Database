package ktx.sovereign.database.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import ktx.sovereign.database.SchemaInfo

@Parcelize
@Entity(tableName = SchemaInfo.Keyword.TableName)
data class Keyword(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SchemaInfo.Keyword.PrimaryKey) val id: Long,
    @ColumnInfo(name = SchemaInfo.Keyword.Term) var term: String,
    @ColumnInfo(name = SchemaInfo.Keyword.Confidence) var confidence: Double,
    @ColumnInfo(name = SchemaInfo.Keyword.Method) var method: Int
) : Parcelable