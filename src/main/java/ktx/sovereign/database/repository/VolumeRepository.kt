package ktx.sovereign.database.repository

import kotlinx.coroutines.*
import ktx.sovereign.database.dao.VolumeDao
import ktx.sovereign.database.entity.Volume
import kotlin.coroutines.CoroutineContext

class VolumeRepository(private val dao: VolumeDao) : CoroutineScope {
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    suspend fun getVolumes(): List<Volume> = withContext(coroutineContext) {
        dao.getAll()
    }

    suspend fun getVolume(id: String): Volume? = withContext(coroutineContext) {
        dao.get(id)
    }

    fun getAllVolumes(): List<Volume> {
        return dao.getAll()
    }

    fun cancel() {
        job.cancelChildren()
    }
}