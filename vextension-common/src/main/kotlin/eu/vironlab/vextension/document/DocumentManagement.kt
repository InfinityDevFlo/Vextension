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

package eu.vironlab.vextension.document

import com.google.gson.JsonElement
import com.google.gson.JsonObject

interface DocumentManagement {

    /**
     * Create a new Empty Document
     * @return an empty Document
     */
    fun createDocument(): Document

    /**
     * Create a create Document by JsonElement
     * @param jsonElement is the JsonElement to create the Document
     * @return the created Document
     */
    fun createDocument(jsonElement: JsonElement): Document

    /**
     * Create a create Document by JsonObject
     * @param jsonObject is the JsonObject, used to create the Document
     * @return the created Document
     */
    fun createDocument(jsonObject: JsonObject): Document

    /**
     * Create a create Document and Insert first key and value
     * @param key is the key to append
     * @param value is the value to append with the key
     * @return the created Document, wich has appended the key and value
     */
    fun createDocument(key: String, value: String): Document

    /**
     * Create a create Document and Insert first key and value
     * @param key is the key to append
     * @param value is the value to append with the key
     * @return the created Document, wich has appended the key and value
     */
    fun createDocument(key: String, value: Number): Document

    /**
     * Create a create Document and Insert first key and value
     * @param key is the key to append
     * @param value is the value to append with the key
     * @return the created Document, wich has appended the key and value
     */
    fun createDocument(key: String, value: Char): Document

    /**
     * Create a create Document and Insert first key and value
     * @param key is the key to append
     * @param value is the value to append with the key
     * @return the created Document, wich has appended the key and value
     */
    fun createDocument(key: String, value: Boolean): Document

    /**
     * Create a create Document and Insert first key and value
     * @param key is the key to append
     * @param value is the value to append with the key
     * @return the created Document, wich has appended the key and value
     */
    fun createDocument(key: String, value: Any): Document

    /**
     * Create a create Document and insert a class instance
     * @param instance is the class instance to parse
     * @return the created Document, wich has appended the values of the class instance
     */
    fun <T> createDocument(instance: T): Document

}