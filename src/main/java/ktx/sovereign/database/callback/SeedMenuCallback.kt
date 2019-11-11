package ktx.sovereign.database.callback

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ktx.sovereign.database.ApplicationDatabase
import ktx.sovereign.database.entity.ScrollingMenuItem

class SeedMenuCallback(
        private val context: Context
) : RoomDatabase.Callback() {
    private val HomeMenuItems: Array<ScrollingMenuItem> = arrayOf(
            ScrollingMenuItem(1, ScrollingMenuItem.HomeModuleCtx, "Select Notes",
                    ScrollingMenuItem.NoteModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_notes", ScrollingMenuItem.AvenirYellow,0),
            ScrollingMenuItem(2, ScrollingMenuItem.HomeModuleCtx, "Select Content",
                    ScrollingMenuItem.ContentModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_content", ScrollingMenuItem.AvenirYellow,1),
            ScrollingMenuItem(3, ScrollingMenuItem.HomeModuleCtx, "Select Gallery",
                    ScrollingMenuItem.GalleryModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_gallery", ScrollingMenuItem.AvenirYellow,2),
            ScrollingMenuItem(4, ScrollingMenuItem.HomeModuleCtx, "Select Reader",
                    ScrollingMenuItem.ReaderModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_ocr", ScrollingMenuItem.AvenirYellow,3),
            ScrollingMenuItem(5, ScrollingMenuItem.HomeModuleCtx, "Select Viewer",
                    ScrollingMenuItem.ViewerModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_augmented_reality", ScrollingMenuItem.AvenirYellow,4),
            ScrollingMenuItem(6, ScrollingMenuItem.HomeModuleCtx, "Select Tasks",
                    ScrollingMenuItem.TaskModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_workflow_management", ScrollingMenuItem.AvenirYellow,5),
            ScrollingMenuItem(7, ScrollingMenuItem.HomeModuleCtx, "Select Camera",
                    ScrollingMenuItem.CameraDeviceCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_camera", ScrollingMenuItem.AvenirYellow,6),
            ScrollingMenuItem(8, ScrollingMenuItem.HomeModuleCtx, "Select Settings",
                    ScrollingMenuItem.SettingsModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_settings", ScrollingMenuItem.AvenirYellow, 7)
    )
    private val ViewerMenuItems: Array<ScrollingMenuItem> = arrayOf(
            ScrollingMenuItem(9, ScrollingMenuItem.ViewerModuleCtx, "Exit Viewer",
                    ScrollingMenuItem.NavigationExitCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_exit", ScrollingMenuItem.AvenirYellow,0),
            ScrollingMenuItem(10, ScrollingMenuItem.ViewerModuleCtx, "Select Notes",
                    ScrollingMenuItem.NoteModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_notes", ScrollingMenuItem.AvenirYellow,1),
            ScrollingMenuItem(11, ScrollingMenuItem.ViewerModuleCtx, "Select Content",
                    ScrollingMenuItem.ContentModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_content", ScrollingMenuItem.AvenirYellow,2),
            ScrollingMenuItem(12, ScrollingMenuItem.ViewerModuleCtx, "Select Media",
                    ScrollingMenuItem.MediaModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_video_asset", ScrollingMenuItem.AvenirYellow,3),
            ScrollingMenuItem(13, ScrollingMenuItem.ViewerModuleCtx, "Select Gallery",
                    ScrollingMenuItem.GalleryModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_gallery", ScrollingMenuItem.AvenirYellow,4),
            ScrollingMenuItem(14, ScrollingMenuItem.ViewerModuleCtx, "Select Reader",
                    ScrollingMenuItem.ReaderModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_ocr", ScrollingMenuItem.AvenirYellow,5),
            ScrollingMenuItem(15, ScrollingMenuItem.ViewerModuleCtx, "Select Tasks",
                    ScrollingMenuItem.TaskModuleCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_workflow_management", ScrollingMenuItem.AvenirYellow,6),
            ScrollingMenuItem(16, ScrollingMenuItem.ViewerModuleCtx, "Select Camera",
                    ScrollingMenuItem.CameraDeviceCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_camera", ScrollingMenuItem.AvenirYellow,7),
            ScrollingMenuItem(17, ScrollingMenuItem.ViewerModuleCtx, "Take Screenshot",
                    ScrollingMenuItem.ActionScreenshotCtx, ScrollingMenuItem.DefaultDirective,
                    "ic_screenshot", ScrollingMenuItem.AvenirYellow, 8)
    )

    override fun onOpen(db: SupportSQLiteDatabase) {
        ApplicationDatabase.getDatabase(context)
                .ScrollingMenuItemDao().apply {
            ApplicationDatabase.DatabaseThread {
                insert(*HomeMenuItems)
                insert(*ViewerMenuItems)
            }
        }
    }
}