package ktx.sovereign.database.contract

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import kotlinx.coroutines.Job
import ktx.sovereign.database.entity.Image
import ktx.sovereign.database.repository.ImageRepository
import ktx.sovereign.database.repository.MediaRepository
import java.io.File

interface MediaStoreContract {
    interface DependencyInjection {
        fun injectImageRepository(): ImageRepository
    }

    interface Controller {
        fun captureImage(activity: Activity, code: Int, file: File? = null)
        fun captureVideo(activity: Activity, code: Int, file: File? = null)
        fun pickImage(activity: Activity, code: Int)
        fun serveFile(activity: Activity, code: Int)
        fun saveImage(context: Context, bitmap: Bitmap): Image
        fun saveVideo(context: Context, data: Uri): Image
        suspend fun registerAttachment(file: File): Image
        suspend fun registerAttachment(contentResolver: ContentResolver, uri: Uri): Image
        fun writeLocationExif(context: Context, image: Image, location: Location)
        fun scanImage(activity: Activity, file: File)
        fun onCancelled(image: Image): Job
        fun onDestroy()
    }
}