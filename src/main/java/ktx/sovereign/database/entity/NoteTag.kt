package ktx.sovereign.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ktx.sovereign.database.SchemaInfo

@Entity(
    tableName = SchemaInfo.NoteTag.TableName,
    primaryKeys = [
        SchemaInfo.NoteTag.Note,
        SchemaInfo.NoteTag.Tag
    ],
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = [SchemaInfo.Note.PrimaryKey],
            childColumns = [SchemaInfo.NoteTag.Note],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MetaTag::class,
            parentColumns = [SchemaInfo.MetaTag.PrimaryKey],
            childColumns = [SchemaInfo.NoteTag.Tag],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NoteTag(
    @ColumnInfo(name = SchemaInfo.NoteTag.Note) val note: Long,
    @ColumnInfo(name = SchemaInfo.NoteTag.Tag) val tag: Long
)