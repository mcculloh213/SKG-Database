package ktx.sovereign.database.storage

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.*
import ktx.sovereign.database.contract.MediaStoreContract
import ktx.sovereign.database.entity.Image
import ktx.sovereign.database.provider.MediaProvider
import ktx.sovereign.database.repository.ImageRepository
import ktx.sovereign.database.repository.MediaRepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class MediaStoreController(
        provider: MediaStoreContract.DependencyInjection
) : MediaStoreContract.Controller, CoroutineScope {
    private val _job: Job = SupervisorJob()
    private val _images: ImageRepository = provider.injectImageRepository()
//    private val _media: MediaRepository = provider.injectMediaRepository()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + _job

    override fun captureImage(activity: Activity, code: Int, file: File?) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(activity.packageManager)?.also {
                val out = file ?: try {
                    MediaProvider.createImageFile(activity)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    null
                }
                out?.also { file ->
                    val uri = FileProvider.getUriForFile(activity, MediaProvider.Authority, file)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    activity.startActivityForResult(intent, code)
                } ?: Toast.makeText(activity, "Could not store photo", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun captureVideo(activity: Activity, code: Int, file: File?) {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { intent ->
            intent.resolveActivity(activity.packageManager)?.also {
                val out = file ?: try {
                    MediaProvider.createVideoFile(activity)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    null
                }
                out?.also { file ->
                    val uri = FileProvider.getUriForFile(activity, MediaProvider.Authority, file)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    activity.startActivityForResult(intent, code)
                } ?: Toast.makeText(activity, "Could not store video", Toast.LENGTH_LONG).show()
            }
        }
//        val uri = FileProvider.getUriForFile(activity, MediaProvider.Authority,
//                MediaProvider.createVideoFile(activity))
//        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
//            putExtra("return-data", true)
//            putExtra(MediaStore.EXTRA_OUTPUT, uri)
//        }
//        return uri.also { activity.startActivityForResult(intent, code) }
    }
    override fun pickImage(activity: Activity, code: Int) {
        Intent(Intent.ACTION_PICK).also { intent ->
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            activity.startActivityForResult(intent, code)
        }
    }
    override fun serveFile(activity: Activity, code: Int) = with (Intent(Intent.ACTION_GET_CONTENT)) {
        type = "*/*"
        addCategory(Intent.CATEGORY_OPENABLE)
        addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                "application/pdf",
                "application/xml",
                "text/xml",
                "text/html",
                "text/plain"
        ))
        if (resolveActivity(activity.packageManager) != null) {
            activity.startActivityForResult(this, code)
        } else {
            Toast.makeText(activity, "Could not open file explorer", Toast.LENGTH_LONG).show()
        }
    }
    override fun saveImage(context: Context, bitmap: Bitmap): Image {
        val file = MediaProvider.createImageFile(context)
        return Image(file.name, file.path).also {
            launch {
                try {
                    with (FileOutputStream(file)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                        flush()
                    }
                    it.id = _images.insert(it)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to save image", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    override suspend fun registerAttachment(file: File): Image = withContext(coroutineContext) {
        Image(file.name, file.path).also {
            it.id = _images.insert(it)
        }
    }

    override suspend fun registerAttachment(contentResolver: ContentResolver, uri: Uri): Image = withContext(coroutineContext) {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val table = StringBuilder()
            if (cursor.moveToFirst()) {
                cursor.columnNames.forEach { col ->
                    table.append("[ $col ]")
                }
                table.append("\n")
                do {
                    cursor.columnNames.forEach { col ->
                        table.append("[ ${cursor.getString(cursor.getColumnIndex(col))} ]")
                    }
                    table.append("\n")
                } while (cursor.moveToNext())
            }
            Log.i("MediaStore", table.toString())
        }
        Image("EXTERNAL", "")
    }

    override fun saveVideo(context: Context, data: Uri): Image {
        val proj = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
        val name: String
        with (context.contentResolver.query(data, proj, null, null, null)) {
            try {
                if (this?.moveToFirst() == true) {
                    name = getString(0)
                } else {
                    throw Exception("Query returned empty cursor.")
                }
            } catch (ex: Exception) {
                this?.close()
                throw Exception("Failed to retrieve video filename.", ex)
            } finally {
                if (this?.isClosed == false) {
                    this.close()
                }
            }
        }
        val video = MediaProvider.getVideoFile(context, name)
        val thumb = MediaProvider.createThumbnailFile(context, name)
        return Image(thumb.name, thumb.path).also {
            launch {
                val bmp = ThumbnailUtils.createVideoThumbnail(video.absolutePath, MediaStore.Video.Thumbnails.MICRO_KIND)
                try {
                    with (FileOutputStream(thumb)) {
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, this)
                        flush()
                    }
                    it.id = _images.insert(it)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to save image", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    override fun writeLocationExif(context: Context, image: Image, location: Location) {
        launch {
            val file = MediaProvider.getImageFile(context, image.display)
            with (ExifInterface(file.absolutePath)) {
                setGpsInfo(location)
                saveAttributes()
            }
        }
    }
    @Deprecated("Action ACTION_MEDIA_SCANNER_SCAN_FILE is deprecated as of SDK 29 (Q)")
    override fun scanImage(activity: Activity, file: File) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { intent ->
            intent.data = Uri.fromFile(file)
            activity.sendBroadcast(intent)
        }
    }
    override fun onCancelled(image: Image): Job = _images.scope.launch {
        _images.delete(image)
    }
    override fun onDestroy() {
        coroutineContext.cancelChildren()
    }
}