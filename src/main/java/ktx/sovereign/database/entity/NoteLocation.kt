package ktx.sovereign.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ktx.sovereign.database.SchemaInfo

@Entity(
    tableName = SchemaInfo.NoteLocation.TableName,
    primaryKeys = [
        SchemaInfo.NoteLocation.Note,
        SchemaInfo.NoteLocation.Location
    ],
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = [SchemaInfo.Note.PrimaryKey],
            childColumns = [SchemaInfo.NoteLocation.Note],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Geolocation::class,
            parentColumns = [SchemaInfo.Geolocation.PrimaryKey],
            childColumns = [SchemaInfo.NoteLocation.Location],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NoteLocation(
    @ColumnInfo(name = SchemaInfo.NoteLocation.Note) val note: Long,
    @ColumnInfo(name = SchemaInfo.NoteLocation.Location) val location: Long
)