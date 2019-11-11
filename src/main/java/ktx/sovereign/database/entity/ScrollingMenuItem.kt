package ktx.sovereign.database.entity

import android.graphics.Color
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ktx.sovereign.database.SchemaInfo

@Parcelize
@Entity(tableName = SchemaInfo.ScrollingMenuItem.TableName)
data class ScrollingMenuItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SchemaInfo.ScrollingMenuItem.PrimaryKey) val id: Long,
    @ColumnInfo(name = SchemaInfo.ScrollingMenuItem.Context) val context: String,
    @ColumnInfo(name = SchemaInfo.ScrollingMenuItem.Label) val label: String,
    @ColumnInfo(name = SchemaInfo.ScrollingMenuItem.Item) val item: String,
    @ColumnInfo(name = SchemaInfo.ScrollingMenuItem.Directive) val directive: String,
    @ColumnInfo(name = SchemaInfo.ScrollingMenuItem.Icon) val icon: String,
    @ColumnInfo(name = SchemaInfo.ScrollingMenuItem.Tint) val tint: Int,
    @ColumnInfo(name = SchemaInfo.ScrollingMenuItem.Ordinal) var ordinal: Int
) : Parcelable {
    companion object {
        @JvmStatic val DefaultDirective: String = "hf_no_number|%s"
        @JvmStatic val AvenirYellow = Color.parseColor("#ffb726")
        @JvmStatic val Transparent = Color.TRANSPARENT

        const val HomeModuleCtx: String = "module:Home"
        const val NoteModuleCtx: String = "module:Notes"
        const val ContentModuleCtx: String = "module:Content"
        const val MediaModuleCtx: String = "module:Media"
        const val GalleryModuleCtx: String = "module:Gallery"
        const val ReaderModuleCtx: String = "module:Reader"
        const val ViewerModuleCtx: String = "module:Viewer"
        const val TaskModuleCtx: String = "external:Tasks"
        const val CameraDeviceCtx: String = "device:Camera"
        const val SettingsModuleCtx: String = "module:Settings"

        const val NavigationExitCtx: String = "navigation:Exit"
        const val ActionScreenshotCtx: String = "action:Screenshot"
    }
    constructor(context: String, label: String, item: String, directive: String, icon: String, tint: Int, ordinal:Int)
            : this(0, context, label, item, directive, icon, tint, ordinal)
}