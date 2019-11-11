package ktx.sovereign.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ktx.sovereign.database.SchemaInfo

@Entity(
    tableName = SchemaInfo.NoteMedia.TableName,
    primaryKeys = [
        SchemaInfo.NoteMedia.Note,
        SchemaInfo.NoteMedia.Media
    ],
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = [SchemaInfo.Note.PrimaryKey],
            childColumns = [SchemaInfo.NoteMedia.Note],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Image::class,
            parentColumns = [SchemaInfo.Image.PrimaryKey],
            childColumns = [SchemaInfo.NoteMedia.Media],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NoteMedia(
    @ColumnInfo(name = SchemaInfo.NoteMedia.Note) val note: Long,
    @ColumnInfo(name = SchemaInfo.NoteMedia.Media) val media: Long
)