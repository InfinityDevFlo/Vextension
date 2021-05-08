/**
 *   Copyright © 2020 | vironlab.eu | All Rights Reserved.<p
 * <p
 *      ___    _______                        ______         ______  <p
 *      __ |  / /___(_)______________ _______ ___  / ______ ____  /_ <p
 *      __ | / / __  / __  ___/_  __ \__  __ \__  /  _  __ `/__  __ \<p
 *      __ |/ /  _  /  _  /    / /_/ /_  / / /_  /___/ /_/ / _  /_/ /<p
 *      _____/   /_/   /_/     \____/ /_/ /_/ /_____/\__,_/  /_.___/ <p
 *<p
 *    ____  _______     _______ _     ___  ____  __  __ _____ _   _ _____ <p
 *   |  _ \| ____\ \   / / ____| |   / _ \|  _ \|  \/  | ____| \ | |_   _|<p
 *   | | | |  _|  \ \ / /|  _| | |  | | | | |_) | |\/| |  _| |  \| | | |  <p
 *   | |_| | |___  \ V / | |___| |__| |_| |  __/| |  | | |___| |\  | | |  <p
 *   |____/|_____|  \_/  |_____|_____\___/|_|   |_|  |_|_____|_| \_| |_|  <p
 *<p
 *<p
 *   This program is free software: you can redistribute it and/or modify<p
 *   it under the terms of the GNU General Public License as published by<p
 *   the Free Software Foundation, either version 3 of the License, or<p
 *   (at your option) any later version.<p
 *<p
 *   This program is distributed in the hope that it will be useful,<p
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of<p
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<p
 *   GNU General Public License for more details.<p
 *<p
 *   You should have received a copy of the GNU General Public License<p
 *   along with this program.  If not, see <http://www.gnu.org/licenses/.<p
 *<p
 *   Contact:<p
 *<p
 *     Discordserver:   https://discord.gg/wvcX92VyEH<p
 *     Website:         https://vironlab.eu/ <p
 *     Mail:            contact@vironlab.eu<p
 *<p
 */


package eu.vironlab.vextension.document

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eu.vironlab.vextension.document.DocumentFactory.documentJsonStorage
import eu.vironlab.vextension.document.DocumentFactory.documentXmlStorage
import eu.vironlab.vextension.document.DocumentFactory.documentYamlStorage
import eu.vironlab.vextension.document.storage.DocumentSpecificStorage
import eu.vironlab.vextension.document.storage.DocumentStorage
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger
import java.util.function.Consumer

/**
 * This class is used for the hole storage and Database System of the Cord. You can get this as ExtensionConfig or when you add/get Properties from many instances.
 * You can store the Document as Json of Yaml
 */
interface Document {
    /**
     * Get the current Document instance as plain Java Object (Any in Kotlin)
     * @return the current instance as Object
     */
    fun toPlainObjects(): Any

    /**
     * Get the current Document instance as a JsonObject from the Google Gson libary
     * @return the current instance as JsonObject
     */
    fun toJsonObject(): JsonObject

    /**
     * Insert a Value identified by the key
     * @param key is the key to identify
     * @param value is the value to append
     * @return the current Document instance
     */
    fun append(key: String, value: Any): Document

    /**
     * Insert a Value identified by the key
     * @param key is the key to identify
     * @param value is the value to append
     * @return the current Document instance
     */
    fun append(key: String, value: Number): Document

    /**
     * Insert a Value identified by the key
     * @param key is the key to identify
     * @param value is the value to append
     * @return the current Document instance
     */
    fun append(key: String, value: Boolean): Document

    /**
     * Insert a Value identified by the key
     * @param key is the key to identify
     * @param value is the value to append
     * @return the current Document instance
     */
    fun append(key: String, value: String): Document

    /**
     * Insert a Value identified by the key
     * @param key is the key to identify
     * @param value is the value to append
     * @return the current Document instance
     */
    fun append(key: String, value: Char): Document

    /**
     * Insert a Value identified by the key
     * @param key is the key to identify
     * @param value is the value to append
     * @return the current Document instance
     */
    fun append(key: String, value: Document): Document

    /**
     * Insert a full document into the existing Document instance
     * @param Document is the Document to append
     * @return the current Document instance
     */
    fun append(Document: Document): Document

    /**
     * Insert a full JsonObject into the existing Document
     * @param jsonObject is the JsonObject to append
     * @return the current Document instance
     */
    fun append(jsonObject: JsonObject): Document


    /**
     * Insert a byte Array identified by a key
     * @param key is the key for identify the array
     * @param bytes is the array to append
     * @return the current Document instance
     */
    fun append(key: String, bytes: ByteArray): Document

    /**
     * Insert the full map into the Document
     * @param map is the Document to append
     * @return the current Document instance
     */
    fun append(map: Map<String, Any>): Document

    /**
     * Insert a JsonElement in the Document
     *
     * @param key is the key for the JsonElement
     * @param value is the JsonElement to append into the Document
     */
    fun append(key: String, value: JsonElement): Document

    /**
     * Get an already appended Document by a key
     * @param key is the key for getting the Document
     * @return the Document if it exists
     */
    fun getDocument(key: String): Document?

    /**
     * Get a Collection of Document identified by key
     * @param key is the key for getting the Documents
     * @return a Collection with the Document if exists
     */
    fun getDocuments(key: String): Collection<Document>?

    /**
     * Get an Integer by a Key
     * @param key is the key of the Integer
     * @return the int if the value exists and is an Integer
     */
    fun getInt(key: String): Int?

    /**
     * Get an Double by a Key
     * @param key is the key of the Double
     * @return the int if the value exists and is an Double
     */
    fun getDouble(key: String): Double?

    /**
     * Get an Float by a Key
     * @param key is the key of the Float
     * @return the int if the value exists and is an Float
     */
    fun getFloat(key: String): Float?

    /**
     * Get an Byte by a Key
     * @param key is the key of the Byte
     * @return the int if the value exists and is an Byte
     */
    fun getByte(key: String): Byte?

    /**
     * Get an Short by a Key
     * @param key is the key of the Short
     * @return the int if the value exists and is an Short
     */
    fun getShort(key: String): Short?

    /**
     * Get an Long by a Key
     * @param key is the key of the Long
     * @return the int if the value exists and is an Long
     */
    fun getLong(key: String): Long?

    /**
     * Get an Boolean by a Key
     * @param key is the key of the Boolean
     * @return the int if the value exists and is an Boolean
     */
    fun getBoolean(key: String): Boolean?

    /**
     * Get an String by a Key
     * @param key is the key of the String
     * @return the int if the value exists and is an String
     */
    fun getString(key: String): String?

    /**
     * Get an Char by a Key
     * @param key is the key of the Char
     * @return the int if the value exists and is an Char
     */
    fun getChar(key: String): Char?

    /**
     * Get an BigDecimal by a Key
     * @param key is the key of the BigDecimal
     * @return the int if the value exists and is an BigDecimal
     */
    fun getBigDecimal(key: String): BigDecimal?

    /**
     * Get an BigInteger by a Key
     * @param key is the key of the BigInteger
     * @return the int if the value exists and is an BigInteger
     */
    fun getBigInteger(key: String): BigInteger?

    /**
     * Get a JsonArray by a Key
     * @param key is the key of the JsonArray
     * @return the JsonArray is exist
     */
    fun getJsonArray(key: String): JsonArray?

    /**
     * Get a JsonObject by Key
     * @param key is the key of the JsonObject
     * @return the JsonObject if exists
     */
    fun getJsonObject(key: String): JsonObject?

    /**
     * Get a ByteArray of a specific Key
     * @param key is the key ti get the Array
     * @return the ByteArray if exist
     */
    fun getBinary(key: String): ByteArray?

    /**
     * Check if the Document is Empty
     */
    fun isEmpty(): Boolean {
        return getKeys().isEmpty()
    }


    /**
     * Get a List of a specific generic type
     * @param key is the key of the List
     * @return the List
     */
    fun <T> getList(key: String): MutableList<T>? {
        return get(key, object : TypeToken<MutableList<T>>() {}.type)
    }

    /**
     * Get a Map of specific generic types
     * @param key is the key of the Map
     * @return the Map
     */
    fun <K, V> getMap(key: String): MutableMap<K, V>? {
        return get(key, object : TypeToken<MutableMap<K, V>>() {}.type)
    }

    /**
     * Get a JsonElement by a key
     * @param key is the key of the element
     * @return the JsonElement
     */
    fun get(key: String): JsonElement?

    /**
     * Get a Value of a Specific class by a key
     * @param key is the Key to the the Value
     * @param clazz is the Class of the Value
     * @param <T> is the Class to parse the Value
     * @return the parsed Value if exist
    </T */
    fun <T> get(key: String, clazz: Class<T>): T?

    /**
     * Get a Value of a Type by the key
     * @param key is the Key to get the Value
     * @param type is the Type of the Value
     * @param <T> is the Class to parse the Value
     * @return the parsed Value if exist
    </T */
    fun <T> get(key: String, type: Type): T?

    /**
     * Get a Value of a Type by the key with default value
     * @param key is the Key to get the Value
     * @param type is the Type of the Value
     * @param <T> is the Class to parse the Value
     * @return the parsed Value if exist or definiton
    </T */
    fun <T> get(key: String, type: Type, def: T): T?

    /**
     * Get a Value of an specific Class by adding a Gson parser to get the Class
     * @param key is the key to get the Value
     * @param gson is the parser to parse the value
     * @param clazz is the class on wich the value will be parsed
     * @param <T> is the Value class
     * @return the Value if exist
    </T */
    operator fun <T> get(key: String, gson: Gson, clazz: Class<T>): T?

    /**
     * Get an Integer and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getInt(key: String, def: Int): Int

    /**
     * Get a Shot and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getShort(key: String, def: Short): Short

    /**
     * Get a Boolean and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getBoolean(key: String, def: Boolean): Boolean

    /**
     * Get a Long and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getLong(key: String, def: Long): Long

    /**
     * Get a Double and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getDouble(key: String, def: Double): Double

    /**
     * Get a Float and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getFloat(key: String, def: Float): Float

    /**
     * Get a List of a specific generic type
     * @param key is the key of the List
     * @return the List
     */
    fun <T> getList(key: String, def: MutableList<T>): MutableList<T> {
        return getList<T>(key) ?: run {
            append(key, def)
            def
        }
    }

    /**
     * Get a Map of specific generic types
     * @param key is the key of the Map
     * @return the Map
     */
    fun <K, V> getMap(key: String, def: MutableMap<K, V>): MutableMap<K, V> {
        return getMap<K, V>(key) ?: run {
            append(key, def)
            def
        }
    }

    /**
     * Get a String and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getString(key: String, def: String): String

    /**
     * Get a Document and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getDocument(key: String, def: Document): Document

    /**
     * Get a Collection with Documents and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getDocuments(key: String, def: Collection<Document>): Collection<Document>

    /**
     * Get a JsonArray and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getJsonArray(key: String, def: JsonArray): JsonArray

    /**
     * Get a JsonObject and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getJsonObject(key: String, def: JsonObject): JsonObject

    /**
     * Get a Byte Array and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getBinary(key: String, def: ByteArray): ByteArray

    /**
     * Get a Class instance and append a value if the key does not exists
     *
     * @param key is the Key to get the Value
     * @param clazz is the Class of the returned instance
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun <T> get(key: String, clazz: Class<T>, def: T): T

    /**
     * Get a BigInteger and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getBigInteger(key: String, def: BigInteger): BigInteger

    /**
     * Get a BigDecimal and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getBigDecimal(key: String, def: BigDecimal): BigDecimal

    /**
     * Get a Char and append a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getChar(key: String, def: Char): Char

    /**
     * @return the current Document as JsonStorage
     */
    fun jsonStorage(): DocumentSpecificStorage {
        return storage(documentJsonStorage)
    }

    /**
     * @return the current Document as YamlStorage
     */
    fun yamlStorage(): DocumentSpecificStorage {
        return storage(documentYamlStorage)
    }

    /**
     * @return the current Document as XMLStorage
     */
    fun xmlStorage(): DocumentSpecificStorage {
        return storage(documentXmlStorage)
    }

    /**
     * Get the Document as SpecificDocumentStorage by an IDocumentStorage instance
     * @param storage is the Storage to get the specific Storage
     * @return the SpecificStorage
     */
    fun storage(storage: DocumentStorage): DocumentSpecificStorage

    /**
     * Execute the Unit to all keys of the Document
     * @param action is the executed Action for each key
     */
    fun forEach(action: (String) -> Unit): Unit

    /**
     * Accept a Consumer to each keys of the Document
     * @param consumer is the Consumer to accept
     */
    fun forEach(consumer: Consumer<String>)

    /**
     * Check if the Document contains a specific Key
     * @param key is the Key for the Check
     *
     * @return is the Document contains the key
     */
    fun contains(key: String): Boolean

    /**
     * Delete a specific value from the Document
     *
     * @param key is the key of the value to Delete
     */
    fun delete(key: String)

    /**
     * Clear the whole Document
     */
    fun clear()

    /**
     * Get the count of all keys in the Document
     * @return the count of keys
     */
    fun size(): Int

    fun <T> toInstance(type: Type): T

    /**
     * Get all keys of the Document
     * @return all keys in a Collection
     */
    fun getKeys(): MutableCollection<String>

    /**
     * Serialize the current Document to String
     */
    fun toJson(): String {
        return jsonStorage().serializeToString()
    }
}