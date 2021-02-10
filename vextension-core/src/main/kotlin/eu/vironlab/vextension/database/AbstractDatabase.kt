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

package eu.vironlab.vextension.database

import eu.vironlab.vextension.concurrent.AsyncTask
import eu.vironlab.vextension.concurrent.scheduleAsync
import eu.vironlab.vextension.database.info.ObjectInformation
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * This class implements the Async methods of the Database and have already the given Information by the Class
 */
abstract class AbstractDatabase<T, K>(val parsedClass: Class<T>) : Database<T, K> {

    override val classInfo: ObjectInformation
        get() = DatabaseUtil.getInfo(parsedClass).orElseThrow { throw IllegalStateException("Cannot get Info of unregistered Object") }

    override fun contains(key: K): Boolean {
        return contains(this.classInfo.key, key!!)
    }

    override fun get(key: K): Optional<T> {
        return if (contains(key)) {
            Optional.of(get(this.classInfo.key, key!!).first())
        }else {
            Optional.empty()
        }
    }

    override fun getAsync(key: K): AsyncTask<Optional<T>> {
        return scheduleAsync { get(key) }
    }

    override fun getAsync(key: String, value: Any): AsyncTask<Collection<T>> {
        return scheduleAsync { get(key, value) }
    }

    override fun insertAsync(key: K, value: T): AsyncTask<Boolean> {
        return scheduleAsync { insert(key, value) }
    }

    override fun deleteAsync(key: K): AsyncTask<Boolean> {
        return scheduleAsync { delete(key) }
    }

    override fun containsAsync(key: K): AsyncTask<Boolean> {
        return scheduleAsync { contains(key) }
    }

    override fun containsAsync(key: String, value: Any): AsyncTask<Boolean> {
        return scheduleAsync { contains(key, value) }
    }

    override fun keysAsync(): AsyncTask<Collection<K>> {
        return scheduleAsync { this.keys() }
    }

    override fun clearAsync(): AsyncTask<Boolean> {
        return scheduleAsync { this.clear() }
    }

    override fun forEachAsync(func: (K, T) -> Unit) {
        scheduleAsync { forEach(func) }
    }

}