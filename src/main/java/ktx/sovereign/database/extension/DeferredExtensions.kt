@file:JvmName("DeferredUtils")
package ktx.sovereign.database.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

infix fun <T> Deferred<T>.then(block: (T) -> Unit): Job {
    return CoroutineScope(Dispatchers.Main).launch {
        block(this@then.await())
    }
}

