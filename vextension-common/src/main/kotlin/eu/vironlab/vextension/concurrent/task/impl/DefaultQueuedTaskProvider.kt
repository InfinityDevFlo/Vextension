package eu.vironlab.vextension.concurrent.task.impl

import eu.vironlab.vextension.concurrent.task.QueuedTask
import eu.vironlab.vextension.concurrent.task.QueuedTaskProvider

class DefaultQueuedTaskProvider : QueuedTaskProvider() {
    override fun <T, R> createTask(callback: (T) -> R, callParam: T): QueuedTask<R> {
        return DefaultQueuedTask<T, R>(callback, callParam)
    }
}
