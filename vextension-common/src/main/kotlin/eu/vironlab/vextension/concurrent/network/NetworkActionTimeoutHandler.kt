package eu.vironlab.vextension.concurrent.network

import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

abstract class  NetworkActionTimeoutHandler {

    companion object {
        @JvmStatic
        val instance: NetworkActionTimeoutHandler = DefaultNetworkActionTimeoutHandler()
    }

    abstract fun handle(action: DefaultNetworkAction<*>, timeout: Long)
}

class DefaultNetworkActionTimeoutHandler : NetworkActionTimeoutHandler() {
    private val check: MutableMap<DefaultNetworkAction<*>, Long> = ConcurrentHashMap()

    init {
        startHandling()
    }

    fun startHandling() {
        thread(true, true) {
            check.entries.filter { it.value < System.currentTimeMillis() }.map { it.key }.forEach {
                check.remove(it)
                it.timeoutAction?.invoke() ?: throw NetworkActionTimedOutException()
            }
            Thread.sleep(10)
        }
    }

    override fun handle(action: DefaultNetworkAction<*>, timeout: Long) {
        this.check[action] = timeout
    }

}

class NetworkActionTimedOutException() : Exception("The Network action timed out")