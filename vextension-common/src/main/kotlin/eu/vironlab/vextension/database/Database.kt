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

import eu.vironlab.vextension.concurrent.task.QueuedTask
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.lang.Nameable

/**
 * All Database Actions return a QueuedTask instance to use the Database Sync or Async -> You can change the QueuedTaskProvider if you dont want to use Coroutines as Provider for the Async Calls
 */
interface Database : Nameable {

    /**
     * Check if [key] is in the Database
     */
    fun contains(key: String): QueuedTask<Boolean>

    /**
     * Check if there is [fieldValue] with the key: [fieldValue] in the Database
     */
    fun contains(fieldName: String, fieldValue: Any): QueuedTask<Boolean>

    /**
     * Get the Value of [key]
     *
     * @return an Optional instance wich is Empty if there was no Object in the Database
     */
    fun get(key: String): QueuedTask<Document?>

    /**
     * Get the Value of the entry from [fieldName] with the value: [fieldValue]
     *
     * @return an Optional instance wich is Empty if there was no Object in the Database
     */
    fun get(fieldName: String, fieldValue: Any): QueuedTask<Collection<Document>>

    /**
     * Get the Value of [key] if present or else insert [def]
     *
     * @see Database.get([key])
     *
     * @return the Document from the Database is present or else [def]
     */
    fun get(key: String, def: Document): QueuedTask<Document>

    /**
     * Update a existing value of [key] to [newValue]
     */
    fun update(key: String, newValue: Document): QueuedTask<Boolean>

    /**
     * Insert [value] identified by [key]
     */
    fun insert(key: String, value: Document): QueuedTask<Boolean>

    /**
     * Delete the Document identified by [key]
     */
    fun delete(key: String): QueuedTask<Boolean>

    /**
     * Delete the Documents identified by [fieldValue] in the [fieldName] Field
     */
    fun delete(fieldName: String, fieldValue: Any): QueuedTask<Boolean>

    /**
     * Get all Key from the Database
     */
    fun keys(): QueuedTask<Collection<String>>

}