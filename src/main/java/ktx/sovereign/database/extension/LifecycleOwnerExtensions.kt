@file:JvmName("LifecycleOwnerUtils")
package ktx.sovereign.database.extension

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import ktx.sovereign.database.coroutine.CoroutineLifecycleListener

fun <T> LifecycleOwner.loadAsync(loader: () -> T): Deferred<T> {
    val deferred = CoroutineScope(Dispatchers.IO).async(start = CoroutineStart.LAZY) {
        loader()
    }
    lifecycle.addObserver(CoroutineLifecycleListener(deferred))
    return deferred
}