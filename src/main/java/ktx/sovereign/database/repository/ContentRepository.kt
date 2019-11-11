package ktx.sovereign.database.repository

import android.os.Environment
import androidx.lifecycle.LiveData
import com.github.slugify.Slugify
import com.industrialbadger.api.IntelligentMixedReality
import com.industrialbadger.api.client.ContentClient
import com.industrialbadger.api.client.S3Client
import kotlinx.coroutines.*
import ktx.sovereign.database.dao.ContentDao
import ktx.sovereign.database.entity.Content
import ktx.sovereign.database.entity.Volume
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import kotlin.coroutines.CoroutineContext

class ContentRepository(private val dao: ContentDao) : CoroutineScope {
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
    val content: LiveData<List<Content>> = dao.getAllContent()

    private val client: ContentClient = IntelligentMixedReality.contentClient
    private val _aws: S3Client = S3Client.create()
    private val slug: Slugify = Slugify()
    fun insertAsync(vararg content: Content) {
        launch {
            dao.insert(*content)
        }
    }
    fun updateAsync(vararg content: Content) {
        launch {
            dao.update(*content)
        }
    }
    fun deleteAsync(vararg content: Content) {
        launch {
            dao.delete(*content)
        }
    }
    fun getAsync(id: String): Content? {
        return dao.get(id)
    }
    suspend fun getContent(): List<Content> = withContext(coroutineContext) {
        dao.getAll()
    }
    suspend fun insert(vararg content: Content) = withContext(coroutineContext) {
        dao.insert(*content)
    }
    suspend fun update(vararg content: Content): Int = withContext(coroutineContext) {
        dao.update(*content)
    }
    suspend fun getAllVolumeContent(token: String): List<Content> = withContext(coroutineContext) {
        dao.getAllVolumeContent(token)
    }

    fun index(volumes: List<Volume>) = launch {
        try {
            volumes.forEach {
                var retry = 0
                var resp: Response<List<com.industrialbadger.api.model.imr.Content.Content>>? = null
                do {
                    try {
                        resp = client.indexApplicationContent(it.token).execute()
                    } catch (ex: SocketTimeoutException) {
                        ex.printStackTrace()
                    }
                    retry += 1
                } while (resp?.isSuccessful != true || retry <= 5)
                resp.let { http ->
                    if (http.isSuccessful) {
//                        http.body()?.forEach { model ->
//                            val data = dao.get(model.id)
//                            if (data == null) {
//                                dao.insert(Content(model.id, model.filename, model.owner, model.application, model.checksum))
//                            } else if (model.checksum != data.checksum) {
//                                with(data) {
//                                    title = model.filename
//                                    owner = model.owner
//                                    token = model.application
//                                    checksum = model.checksum
//                                }
//                                dao.update(data)
//                            }
//                        }
                    }
                }
            }
        } catch (ex: UnknownHostException) {
            ex.printStackTrace()
        } catch (ex: SSLException) {
            ex.printStackTrace()
        }
    }
    suspend fun read(content: Content): String = withContext(coroutineContext) {
        var retry = 0
        var resp: Response<ResponseBody>? = null
//        do {
//            try {
//                resp = client.read("motoman", content.owner, content.token, content.title).execute()
//            } catch (ex: SocketTimeoutException) {
//                ex.printStackTrace()
//            }
//            retry += 1
//        } while (resp?.isSuccessful != true || retry <= 5)
        resp?.let {
            if (it.isSuccessful) {
                it.body()?.string()
                        ?: "<h1>404: File not Found</h1><p>You are seeing this message because the file found at <span style=\"color: rgb(255, 0, 0);\">${content.token}/${content.title}</span> could not be found. Please refresh your content and try again.</p>"
            } else {
                it.errorBody()?.string()
                        ?: "<h1>404: File not Found</h1><p>You are seeing this message because the file found at <span style=\"color: rgb(255, 0, 0);\">${content.token}/${content.title}</span> could not be found. Please refresh your content and try again.</p>"
            }
        } ?: ""
    }
    suspend fun save(content: Content, token: String): Boolean = withContext(coroutineContext) {
        try {
            // Fetch file data
            val resp = _aws.getFileAsync("edutechnologic.motoman", token, S3Client.sanitize(content.title))
            if (resp.isSuccessful && resp.body() != null) {
                // Write the file
                val name = "${slugify(content.title.substringBeforeLast('.'))}.html"
                val file = File(getVolumeDirectory(content.token), name)
                resp.body()?.byteStream()?.use { input ->
                    file.outputStream().use { input.copyTo(it) }
                }
//                with(content) {
//                    path = "${content.token}/$name"
//                }
                updateAsync(content)
                true
            } else {
                false
            }
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun readFile(content: Content): String = withContext(coroutineContext) {
        try {
            val builder = StringBuilder()
            with (File(getContentDirectory(), content.path)) {
                bufferedReader().readLines().forEach {
                    builder.append(it).append('\n')
                }
            }
            builder.toString()
        } catch (ex: Exception) {
            """
                <h1>${ex::class.simpleName}</h1>
                <p>${ex.message}</p>
            """.trimIndent()
        }
    }

    fun cancel() {
        job.cancelChildren()
    }

    @Throws(IOException::class)
    private fun getContentDirectory(): File = File(Environment.getExternalStorageDirectory(), "Content").apply {
                if (!exists() && !mkdirs()) {
                    throw IOException("Unable to create directory 'Content'.")
                }
            }

    @Throws(IOException::class)
    private fun getVolumeDirectory(token: String): File = File(getContentDirectory(), token).apply {
                if (!exists() && !mkdirs()) {
                    throw IOException("Unable to create content directory for $token.")
                }
            }

    private fun slugify(name: String): String = slug.slugify(name)
}