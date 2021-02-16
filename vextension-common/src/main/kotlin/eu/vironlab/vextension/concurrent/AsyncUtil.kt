package eu.vironlab.vextension.concurrent

import java.util.concurrent.Callable
import java.util.concurrent.Executors

/**
 * Get the Async Task of a Callable
 */
fun <T> scheduleAsync(callable: Callable<T>): AsyncTask<T> {
    val task: AsyncTask<T> = DefaultAsyncTask(callable)
    Executors.newSingleThreadExecutor().execute {
        try {
            Thread.sleep(0, 100000)
            task.call()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
    return task
}