package ktx.sovereign.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.withContext
import ktx.sovereign.database.dao.ImageDao
import ktx.sovereign.database.entity.Image
import ktx.sovereign.database.provider.MediaProvider
import java.io.File

class ImageRepository(private val dao: ImageDao) : EntityRepository() {
    val images: LiveData<List<Image>> = dao.getAllImages()
    suspend fun index(context: Context): Array<File> = withContext(coroutineContext) {
        MediaProvider.getExternalPicturesDir(context).listFiles()
    }
    suspend fun insert(image: Image): Long = withContext(scope.coroutineContext) { dao.insert(image) }
    suspend fun delete(image: Image): Int = withContext(scope.coroutineContext) { dao.delete(image) }
}