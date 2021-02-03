package eu.vironlab.vextension.concurrent

import java.util.concurrent.Callable
import java.util.concurrent.Executors

object AsyncUtil {
    /**
     * Schedule a Callable
     *
     * @param callable is the Callable
     * @param <T> is the Value of the Task to return
     * @return the Task of calling the Callable
    </T> */
    @JvmStatic
    fun <T> schedule(callable: Callable<T>): AsyncTask<T> {
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
}