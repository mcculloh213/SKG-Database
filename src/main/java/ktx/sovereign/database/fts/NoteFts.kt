package ktx.sovereign.database.fts

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import ktx.sovereign.database.entity.Note
import ktx.sovereign.database.SchemaInfo

@Entity(tableName = SchemaInfo.NoteFts.TableName)
@Fts4(
    contentEntity = Note::class,
    tokenizer = FtsOptions.TOKENIZER_PORTER
)
@Parcelize
class NoteFts (
    @PrimaryKey
    @ColumnInfo(name = SchemaInfo.NoteFts.PrimaryKey) val rowid: Int,
    @ColumnInfo(name = SchemaInfo.Note.Title) val title: String,
    @ColumnInfo(name = SchemaInfo.Note.Body) val body: String
) : Parcelable