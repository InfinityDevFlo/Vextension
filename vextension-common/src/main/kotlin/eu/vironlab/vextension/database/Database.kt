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
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.lang.Nameable
import java.util.*

/**
 * A Wrapper for Multiple Database Types
 */
interface Database : Nameable {

    /**
     * Get the Value of [key] in the Database as Optional instance
     *
     * @return the Optional instance
     */
    fun get(key: String): Optional<Document>

    /**
     * Get all value with the [key] and [value]
     *
     * @return All matching things
     */
    fun get(key: String, value: Any): Collection<Document>

    /**
     * Get a value or set a Default
     */
    fun getOrDefault(key: String, definition: Document): Document

    /**
     * Insert [value] into the Database identified by [key]
     *
     * @return if the Value can be inserted or the was an Error or the Key already exists
     */
    fun insert(key: String, value: Document): Boolean

    /**
     * Update a the value of [key] to [newValue]
     *
     * @return if the Value can be replaces
     */
    fun update(key: String, newValue: Document): Boolean

    /**
     * Delete the Value of [key]
     *
     * @return if the Value can be deleted
     */
    fun delete(key: String): Boolean

    /**
     * Check if the Database contains [key]
     *
     * @return the result of the Check
     */
    fun contains(key: String): Boolean

    /**
     * Check if [value] is the the Database identified by [key]
     *
     * @return the result of the Check
     */
    fun contains(key: String, value: Any): Boolean

    /**
     * Get all keys of the Database
     *
     * @return all keys as Collection
     */
    fun keys(): Collection<String>

    /**
     * Clear the whole Database
     *
     * @return if there was an Error while deleting the Data
     */
    fun clear(): Boolean

    /**
     * Execute a Unit for all Elements in the Database
     */
    fun forEach(func: (String, Document) -> Unit)

    /**
     * @see Database.get([key]) as Async method
     *
     * @return the Result as AsyncTask
     */
    fun getAsync(key: String): AsyncTask<Optional<Document>>

    /**
     * @see Database.get([key], [value]) as Async method
     *
     * @return the Result as AsyncTask
     */
    fun getAsync(key: String, value: Any): AsyncTask<Collection<Document>>

    /**
     * @see Database.insert([key], [value]) as Async method
     *
     * @return the Result as AsyncTask
     */
    fun insertAsync(key: String, value: Document): AsyncTask<Boolean>

    /**
     * @see Database.delete([key]) as Async method
     *
     * @return the Result as AsyncTask
     */
    fun deleteAsync(key: String): AsyncTask<Boolean>

    /**
     * @see Database.contains([key]) as Async method
     *
     * @return the Result as AsyncTask
     */
    fun containsAsync(key: String): AsyncTask<Boolean>

    /**
     * @see Database.keys as Async method
     *
     * @return the Result as AsyncTask
     */
    fun keysAsync(): AsyncTask<Collection<String>>

    /**
     * @see Database.contains([key], [value]) as Async method
     *
     * @return the Result as AsyncTask
     */
    fun containsAsync(key: String, value: Any): AsyncTask<Boolean>

    /**
     * @see Database.forEach([func]) as Async method
     *
     * @return the Result as AsyncTask
     */
    fun forEachAsync(func: (String, Document) -> Unit)

    /**
     * @see Database.clear as Async method
     *
     * @return the Result as AsyncTask
     */
    fun clearAsync(): AsyncTask<Boolean>

    /**
     * @see Database.update([key], [value]) as Async method
     *
     * @return the Result as AsyncTask
     */
    fun updateAsync(key: String, Documentvalue: Document): AsyncTask<Boolean>

}