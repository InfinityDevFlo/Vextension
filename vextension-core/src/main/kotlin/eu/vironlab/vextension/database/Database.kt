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
import eu.vironlab.vextension.lang.Nameable
import java.util.*
import java.util.function.BiConsumer

interface Database<T> : Nameable {

    fun insert(key: String, obj: T): Boolean

    fun get(key: String): Optional<T>

    fun get(field: String, value: String): Collection<T>

    fun getAllObjects(): MutableMap<String, T>

    fun keys(): Collection<String>

    fun update(key: String, newObj: T): Boolean

    fun delete(key: String): Boolean

    fun contains(key: String): Boolean

    fun clear(): Boolean

    fun getDocumentsCount(): Long

    fun forEach(consumer: BiConsumer<String, T>): Boolean

    fun insertAsync(key: String, obj: T): AsyncTask<Boolean>

    fun getAsync(key: String): AsyncTask<Optional<T>>

    fun getAsync(field: String, value: String): AsyncTask<Collection<T>>

    fun getAllObjectsAsync(): AsyncTask<MutableMap<String, T>>

    fun keysAsync(): AsyncTask<Collection<String>>

    fun updateAsync(key: String, newObj: T): AsyncTask<Boolean>

    fun deleteAsync(key: String): AsyncTask<Boolean>

    fun containsAsync(key: String): AsyncTask<Boolean>

    fun clearAsync(): AsyncTask<Boolean>

    fun getDocumentsCountAsync(): AsyncTask<Long>

    fun forEachAsync(consumer: BiConsumer<String, T>): AsyncTask<Boolean>


}

abstract class AbstractDatabase<T>(override val name: String) : Database<T> {
    override fun insertAsync(key: String, obj: T): AsyncTask<Boolean> {
        return scheduleAsync { this.insert(key, obj) }
    }

    override fun getAsync(key: String): AsyncTask<Optional<T>> {
        return scheduleAsync { this.get(key) }
    }

    override fun getAsync(field: String, value: String): AsyncTask<Collection<T>> {
        return scheduleAsync { this.get(field, value) }
    }

    override fun getAllObjectsAsync(): AsyncTask<MutableMap<String, T>> {
        return scheduleAsync { this.getAllObjects() }
    }

    override fun keysAsync(): AsyncTask<Collection<String>> {
        return scheduleAsync { this.keys() }
    }

    override fun updateAsync(key: String, newObj: T): AsyncTask<Boolean> {
        return scheduleAsync { this.update(key, newObj) }
    }

    override fun deleteAsync(key: String): AsyncTask<Boolean> {
        return scheduleAsync { this.delete(key) }
    }

    override fun containsAsync(key: String): AsyncTask<Boolean> {
        return scheduleAsync { this.contains(key) }
    }

    override fun clearAsync(): AsyncTask<Boolean> {
        return scheduleAsync { this.clear() }
    }

    override fun getDocumentsCountAsync(): AsyncTask<Long> {
        return scheduleAsync { this.getDocumentsCount() }
    }

    override fun forEachAsync(consumer: BiConsumer<String, T>): AsyncTask<Boolean> {
        return scheduleAsync { this.forEach(consumer) }
    }


}