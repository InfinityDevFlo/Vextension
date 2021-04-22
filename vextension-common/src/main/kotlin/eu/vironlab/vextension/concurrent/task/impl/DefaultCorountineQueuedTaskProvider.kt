package eu.vironlab.vextension.concurrent.task.impl

import eu.vironlab.vextension.concurrent.task.QueuedTask
import eu.vironlab.vextension.concurrent.task.QueuedTaskProvider
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DefaultCorountineQueuedTaskProvider : QueuedTaskProvider() {
    override fun <R> createTask(callback: (Unit) -> R): QueuedTask<R> {
        return DefaultCorountineQueuedTask<R>(callback)
    }
}
