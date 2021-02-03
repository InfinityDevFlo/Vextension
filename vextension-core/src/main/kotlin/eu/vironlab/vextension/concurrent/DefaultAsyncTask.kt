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
 *   Contact:<p>
 *<p>
 *     Discordserver:   https://discord.gg/wvcX92VyEH<p>
 *     Website:         https://vironlab.eu/ <p>
 *     Mail:            contact@vironlab.eu<p>
 *<p>
 */

package eu.vironlab.vextension.concurrent

import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.function.BiConsumer
import java.util.function.Consumer
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


/**
 * This is an Implementation of the IAsyncTask
 *
 * @see AsyncTask
 *
 *
 * @param <T> if the Type of the Task
</T> */
class DefaultAsyncTask<T>(private val callable: Callable<T>) : AsyncTask<T> {
    private var done = false
    private var cancelled = false
    private var completeHandler: BiConsumer<AsyncTask<T>, T>? = null
    private var cancelHandler: Consumer<AsyncTask<T>>? = null
    private var failHandler: Consumer<AsyncTask<T>>? = null
    private var value: T? = null

    @Throws(Exception::class)
    override fun call(): T {
        if (!isCancelled) {
            value = callable.call()
        }
        done = true
        return value!!
    }

    override fun cancel(b: Boolean): Boolean {
        return b.also { cancelled = it }
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    override fun get(): T {
        synchronized(this) {
            if (!isDone) {
                runBlocking {
                    delay(0L)
                }
            }
        }
        return value!!
    }

    @Throws(InterruptedException::class, ExecutionException::class, TimeoutException::class)
    override fun get(l: Long, timeUnit: TimeUnit): T {
        synchronized(this) {
            if (!isDone) {
                runBlocking {
                    delay(timeUnit.toMillis(l))
                }
            }
        }
        return value!!
    }

    override fun isCancelled(): Boolean {
        return this.cancelled
    }

    override fun isDone(): Boolean {
        return this.done
    }

    override fun onComplete(consumer: BiConsumer<AsyncTask<T>, T>?): AsyncTask<T> {
        this.completeHandler = consumer
        return this
    }

    override fun onCancel(consumer: Consumer<AsyncTask<T>>?): AsyncTask<T> {
        this.cancelHandler = consumer
        return this
    }

    override fun onFail(consumer: Consumer<AsyncTask<T>>?): AsyncTask<T> {
        this.failHandler = consumer
        return this
    }
}