package ktx.sovereign.database.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class EntityRepository : CoroutineScope {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    fun cancel() {
        job.cancel()
    }
}