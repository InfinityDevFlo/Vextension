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

package eu.vironlab.vextension.database.sql

import eu.vironlab.vextension.concurrent.AsyncTask
import eu.vironlab.vextension.database.Database
import eu.vironlab.vextension.database.DatabaseObject
import java.util.*
import java.util.function.BiConsumer


class SqlDatabase<T: DatabaseObject>(
    override val name: String,
    val client: SqlDatabaseClient
) : Database<T> {

    override fun insert(key: String, obj: T): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(key: String): Optional<T> {
        TODO("Not yet implemented")
    }

    override fun get(field: String, value: String): Collection<T> {
        TODO("Not yet implemented")
    }

    override fun getAllObjects(): MutableMap<String, T> {
        TODO("Not yet implemented")
    }

    override fun keys(): Collection<String> {
        TODO("Not yet implemented")
    }

    override fun update(key: String, newObj: T): Boolean {
        TODO("Not yet implemented")
    }

    override fun delete(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDocumentsCount(): Long {
        TODO("Not yet implemented")
    }

    override fun forEach(consumer: BiConsumer<String, T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun insertAsync(key: String, obj: T): AsyncTask<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getAsync(key: String): AsyncTask<Optional<T>> {
        TODO("Not yet implemented")
    }

    override fun getAsync(field: String, value: String): AsyncTask<Collection<T>> {
        TODO("Not yet implemented")
    }

    override fun getAllObjectsAsync(): AsyncTask<MutableMap<String, T>> {
        TODO("Not yet implemented")
    }

    override fun keysAsync(): AsyncTask<Collection<String>> {
        TODO("Not yet implemented")
    }

    override fun updateAsync(key: String, newObj: T): AsyncTask<Boolean> {
        TODO("Not yet implemented")
    }

    override fun deleteAsync(key: String): AsyncTask<Boolean> {
        TODO("Not yet implemented")
    }

    override fun containsAsync(key: String): AsyncTask<Boolean> {
        TODO("Not yet implemented")
    }

    override fun clearAsync(): AsyncTask<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getDocumentsCountAsync(): AsyncTask<Long> {
        TODO("Not yet implemented")
    }

    override fun forEachAsync(consumer: BiConsumer<String, T>): AsyncTask<Boolean> {
        TODO("Not yet implemented")
    }
}