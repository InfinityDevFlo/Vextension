package eu.vironlab.vextension.concurrent.network

import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

object NetworkActionTimeoutHandler {
    @JvmStatic
    private val check: MutableMap<DefaultNetworkAction<*>, Long> = ConcurrentHashMap()

    init {
        startHandling()
    }

    @JvmStatic
    fun startHandling() {
        thread(true, true) {
            check.entries.filter { it.value < System.currentTimeMillis() }.map { it.key }.forEach {
                check.remove(it)
                it.timeoutAction?.invoke() ?: throw NetworkActionTimedOutException()
            }
            Thread.sleep(10)
        }
    }

    @JvmStatic
    fun handle(action: DefaultNetworkAction<*>, timeout: Long) {
        this.check[action] = timeout
    }

}

class NetworkActionTimedOutException() : Exception("The Network action timed out")