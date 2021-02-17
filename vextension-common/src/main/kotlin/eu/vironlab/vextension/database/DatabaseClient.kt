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
import eu.vironlab.vextension.document.DefaultDocument
import eu.vironlab.vextension.document.Document

/**
 * Client to Connect to the Database <p>
 *
 * You can manage the Used Client instance with the Vextension Class <p>
 *
 * An instance of this class is to connect your Projekt to a Database. For Sql and Mongo Databases
 * you can use the Following Clients: <p>
 *
 */
interface DatabaseClient {

    /**
     * This method is called when the Client will be initialized
     */
    fun init()

    /**
     * Use this Method to get a Database instance from the Client <p>
     *
     * If you dont want to parse the Data to a specific Class instance you can use the getBasicDatabase() method <p>
     *
     * @param parsedClass is the Class wich instances will be used by parse Data from the Database or Insert them. The class has to extend the
     * { @link eu.vironlab.vextension.database.DatabaseObject } class
     *
     * @return The Database instance, wich will be created if the Database does not exists
     */
    fun <T, K> getDatabase(name: String, parsedClass: Class<T>): Database<T, K>

    /**
     * Get a Database with the Document
     *
     * @param name is the Name of the Database
     *
     * @return a Database with a Document as DatabaseObject
     *
     * @see DatabaseClient.getDatabase
     */
    fun getBasicDatabase(name: String): Database<DefaultDocument, String>

    /**
     * Async Method for
     * @see DatabaseClient.getDatabase
     */
    fun <T, K> getDatabaseAsync(name: String, parsedClass: Class<T>): AsyncTask<Database<T, K>>

    /**
     * Async Method for
     * @see DatabaseClient.getBasicDatabase
     */
    fun getBasicDatabaseAsync(name: String): AsyncTask<Database<out Document, String>>

    /**
     * Check if a Database with the given name exists
     *
     * @param name is the Name of the Database
     *
     * @return the result of the Check
     */
    fun exists(name: String): Boolean

    /**
     * Delete a Database
     *
     * @param name is the Name of the Database
     */
    fun drop(name: String): Boolean
}
