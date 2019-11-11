package ktx.sovereign.database.entity

import android.os.Parcelable
import android.os.SystemClock
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ktx.sovereign.database.SchemaInfo

@Parcelize
@Entity(tableName = SchemaInfo.Message.TableName)
data class Message(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SchemaInfo.Message.PrimaryKey) val id: Long,
    @ColumnInfo(name = SchemaInfo.Message.Conversation) val conversation: String,
    @ColumnInfo(name = SchemaInfo.Message.To) val to: String,
    @ColumnInfo(name = SchemaInfo.Message.From) val from: String,
    @ColumnInfo(name = SchemaInfo.Message.Text) val text: String,
    @ColumnInfo(name = SchemaInfo.Message.Timestamp) val timestamp: Long
) : Parcelable {
    @Ignore
    constructor(to: String, from: String, text: String) :
            this(0, "", to, from, text, SystemClock.elapsedRealtimeNanos() / 1_000)
}