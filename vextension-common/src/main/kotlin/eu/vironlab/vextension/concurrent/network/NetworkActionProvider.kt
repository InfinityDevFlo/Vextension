/**
 *   Copyright © 2020 | vironlab.eu | All Rights Reserved.<p>
 * <p>
 *      ___    _______                        ______         ______  <p>
 *      __ |  / /___(_)______________ _______ ___  / ______ ____  /_ <p>
 *      __ | / / __  / __  ___/_  __ \__  __ \__  /  _  __ `/__  __ \<p>
 *      __ |/ /  _  /  _  /    / /_/ /_  / / /_  /___/ /_/ / _  /_/ /<p>
 *      _____/   /_/   /_/     \____/ /_/ /_/ /_____/\__,_/  /_.___/ <p>
 *<p>
 *    ____  _______     _______ _     ___  ____  __  __ _____ _   _ _____ <p>
 *   |  _ \| ____\ \   / / ____| |   / _ \|  _ \|  \/  | ____| \ | |_   _|<p>
 *   | | | |  _|  \ \ / /|  _| | |  | | | | |_) | |\/| |  _| |  \| | | |  <p>
 *   | |_| | |___  \ V / | |___| |__| |_| |  __/| |  | | |___| |\  | | |  <p>
 *   |____/|_____|  \_/  |_____|_____\___/|_|   |_|  |_|_____|_| \_| |_|  <p>
 *<p>
 *<p>
 *   This program is free software: you can redistribute it and/or modify<p>
 *   it under the terms of the GNU General Public License as published by<p>
 *   the Free Software Foundation, either version 3 of the License, or<p>
 *   (at your option) any later version.<p>
 *<p>
 *   This program is distributed in the hope that it will be useful,<p>
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of<p>
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<p>
 *   GNU General Public License for more details.<p>
 *<p>
 *   You should have received a copy of the GNU General Public License<p>
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.<p>
 *<p>
 *   Creation: Sonntag 13 Juni 2021 12:03:56<p>
 *<p>
 *   Contact:<p>
 *<p>
 *     Discordserver:   https://discord.gg/wvcX92VyEH<p>
 *     Website:         https://vironlab.eu/ <p>
 *     Mail:            contact@vironlab.eu<p>
 *<p>
 */

package eu.vironlab.vextension.concurrent.network

import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.Executors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class NetworkActionProvider {

    companion object {
        @JvmStatic
        var instance: NetworkActionProvider = DefaultCorountineTaskProvider()
    }

    abstract fun <T> createTask(action: () -> T): NetworkAction<T>

}

class DefaultCorountineTaskProvider : NetworkActionProvider() {

    val scope = CoroutineScope(
        Executors.newCachedThreadPool(ThreadFactoryBuilder().setNameFormat("NetworkAction-%d").build())
            .asCoroutineDispatcher()
    )

    override fun <T> createTask(action: () -> T): NetworkAction<T> = DefaultNetworkAction<T>(action, scope)
}

class DefaultNetworkAction<T>(val action: () -> T, val scope: CoroutineScope) : NetworkAction<T> {

    var errorAction: ((Throwable) -> Unit)? = null
    var timeoutAction: (() -> Unit)? = null
    var timeout: Long = 60000

    override fun queue(): DefaultNetworkAction<T> {
        scope.launch {
            try {
                NetworkActionTimeoutHandler.instance.handle(this@DefaultNetworkAction, timeout)
                action.invoke()
            } catch (e: Throwable) {
                errorAction?.invoke(e)
            }
        }
        return this
    }

    override fun queue(resultAction: (T) -> Unit): DefaultNetworkAction<T> {
        scope.launch {
            try {
                NetworkActionTimeoutHandler.instance.handle(this@DefaultNetworkAction, timeout)
                resultAction.invoke(action.invoke())
            } catch (e: Throwable) {
                errorAction?.invoke(e)
            }
        }
        return this
    }

    override fun queueAfter(time: Long): DefaultNetworkAction<T> {
        scope.launch {
            delay(time)
            try {
                NetworkActionTimeoutHandler.instance.handle(this@DefaultNetworkAction, timeout)
                action.invoke()
            } catch (e: Throwable) {
                errorAction?.invoke(e)
            }
        }
        return this
    }

    override fun queueAfter(time: Long, resultAction: (T) -> Unit): DefaultNetworkAction<T> {
        scope.launch {
            delay(time)
            try {
                NetworkActionTimeoutHandler.instance.handle(this@DefaultNetworkAction, timeout)
                action.invoke()
            } catch (e: Throwable) {
                errorAction?.invoke(e)
            }
        }
        return this
    }

    override fun complete(): T {
        try {
            NetworkActionTimeoutHandler.instance.handle(this@DefaultNetworkAction, timeout)
            return action.invoke()
        } catch (e: Throwable) {
            errorAction?.invoke(e)
            throw e
        }
    }

    override fun complete(resultAction: (T) -> Unit): DefaultNetworkAction<T> {
        try {
            NetworkActionTimeoutHandler.instance.handle(this@DefaultNetworkAction, timeout)
            resultAction.invoke(action.invoke())
        } catch (e: Throwable) {
            errorAction?.invoke(e)
        }
        return this
    }

    override fun <C> complete(returnCallback: (T) -> C): C {
        try {
            NetworkActionTimeoutHandler.instance.handle(this@DefaultNetworkAction, timeout)
            return returnCallback(action.invoke())
        } catch (e: Throwable) {
            errorAction?.invoke(e)
            throw e
        }
    }

    override fun onError(action: (Throwable) -> Unit): DefaultNetworkAction<T> {
        this.errorAction = action
        return this
    }

    override fun onTimeout(action: () -> Unit): NetworkAction<T> {
        this.timeoutAction = action
        return this
    }

    override fun setTimeout(timeout: Long): NetworkAction<T> {
        this.timeout = timeout
        return this
    }
}