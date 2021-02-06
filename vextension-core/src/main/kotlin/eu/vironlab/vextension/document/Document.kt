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

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eu.vironlab.vextension.document.storage.DocumentStorage
import eu.vironlab.vextension.document.storage.SpecificDocumentStorage
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
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
     * @param value is the value to insert
     * @return the current Document instance
     */
    fun insert(key: String, value: Any): Document

    /**
     * Insert a Value identified by the key
     * @param key is the key to identify
     * @param value is the value to insert
     * @return the current Document instance
     */
    fun insert(key: String, value: Number): Document

    /**
     * Insert a Value identified by the key
     * @param key is the key to identify
     * @param value is the value to insert
     * @return the current Document instance
     */
    fun insert(key: String, value: Boolean): Document

    /**
     * Insert a Value identified by the key
     * @param key is the key to identify
     * @param value is the value to insert
     * @return the current Document instance
     */
    fun insert(key: String, value: String): Document

    /**
     * Insert a Value identified by the key
     * @param key is the key to identify
     * @param value is the value to insert
     * @return the current Document instance
     */
    fun insert(key: String, value: Char): Document

    /**
     * Insert a Value identified by the key
     * @param key is the key to identify
     * @param value is the value to insert
     * @return the current Document instance
     */
    fun insert(key: String, value: Document): Document

    /**
     * Insert a full document into the existing Document instance
     * @param Document is the Document to insert
     * @return the current Document instance
     */
    fun insert(Document: Document): Document

    /**
     * Insert a full JsonObject into the existing Document
     * @param jsonObject is the JsonObject to insert
     * @return the current Document instance
     */
    fun insert(jsonObject: JsonObject): Document

    /**
     * Insert Properties into the Document
     * @param properties are the properties to insert
     * @return the current Document instance
     */
    fun insert(properties: Properties): Document

    /**
     * Insert Properties into the Document identified by a Key
     * @param key is the key for identify the Properties
     * @param properties are the properties to insert
     * @return the current Document instance
     */
    fun insert(key: String, properties: Properties): Document

    /**
     * Insert a byte Array identified by a key
     * @param key is the key for identify the array
     * @param bytes is the array to insert
     * @return the current Document instance
     */
    fun insert(key: String, bytes: ByteArray): Document

    /**
     * Insert the full map into the Document
     * @param map is the Document to insert
     * @return the current Document instance
     */
    fun insert(map: Map<String, Any>): Document

    /**
     * Insert a JsonElement in the Document
     *
     * @param key is the key for the JsonElement
     * @param value is the JsonElement to insert into the Document
     */
    fun insert(key: String, value: JsonElement): Document

    /**
     * Get an already inserted Document by a key
     * @param key is the key for getting the Document
     * @return the Document if it exists
     */
    fun getDocument(key: String): Optional<Document>

    /**
     * Get a Collection of Document identified by key
     * @param key is the key for getting the Documents
     * @return a Collection with the Document if exists
     */
    fun getDocuments(key: String): Optional<Collection<Document>>

    /**
     * Get an Integer by a Key
     * @param key is the key of the Integer
     * @return the int if the value exists and is an Integer
     */
    fun getInt(key: String): Optional<Int>

    /**
     * Get an Double by a Key
     * @param key is the key of the Double
     * @return the int if the value exists and is an Double
     */
    fun getDouble(key: String): Optional<Double>

    /**
     * Get an Float by a Key
     * @param key is the key of the Float
     * @return the int if the value exists and is an Float
     */
    fun getFloat(key: String): Optional<Float>

    /**
     * Get an Byte by a Key
     * @param key is the key of the Byte
     * @return the int if the value exists and is an Byte
     */
    fun getByte(key: String): Optional<Byte>

    /**
     * Get an Short by a Key
     * @param key is the key of the Short
     * @return the int if the value exists and is an Short
     */
    fun getShort(key: String): Optional<Short>

    /**
     * Get an Long by a Key
     * @param key is the key of the Long
     * @return the int if the value exists and is an Long
     */
    fun getLong(key: String): Optional<Long>

    /**
     * Get an Boolean by a Key
     * @param key is the key of the Boolean
     * @return the int if the value exists and is an Boolean
     */
    fun getBoolean(key: String): Optional<Boolean>

    /**
     * Get an String by a Key
     * @param key is the key of the String
     * @return the int if the value exists and is an String
     */
    fun getString(key: String): Optional<String>

    /**
     * Get an Char by a Key
     * @param key is the key of the Char
     * @return the int if the value exists and is an Char
     */
    fun getChar(key: String): Optional<Char>

    /**
     * Get an BigDecimal by a Key
     * @param key is the key of the BigDecimal
     * @return the int if the value exists and is an BigDecimal
     */
    fun getBigDecimal(key: String): Optional<BigDecimal>

    /**
     * Get an BigInteger by a Key
     * @param key is the key of the BigInteger
     * @return the int if the value exists and is an BigInteger
     */
    fun getBigInteger(key: String): Optional<BigInteger>

    /**
     * Get a JsonArray by a Key
     * @param key is the key of the JsonArray
     * @return the JsonArray is exist
     */
    fun getJsonArray(key: String): Optional<JsonArray>

    /**
     * Get a JsonObject by Key
     * @param key is the key of the JsonObject
     * @return the JsonObject if exists
     */
    fun getJsonObject(key: String): Optional<JsonObject>

    /**
     * Get Properties of a specific Key
     * @param key is the Key to get the Properties
     * @return the Properties if exists
     */
    fun getProperties(key: String): Optional<Properties>

    /**
     * Get a ByteArray of a specific Key
     * @param key is the key ti get the Array
     * @return the ByteArray if exist
     */
    fun getBinary(key: String): Optional<ByteArray>

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
    fun <T> getList(key: String): Optional<MutableList<T>> {
        return get(key, object : TypeToken<MutableList<T>>() {}.type)
    }

    /**
     * Get a JsonElement by a key
     * @param key is the key of the element
     * @return the JsonElement
     */
    fun get(key: String): Optional<JsonElement>

    /**
     * Get a Value of a Specific class by a key
     * @param key is the Key to the the Value
     * @param clazz is the Class of the Value
     * @param <T> is the Class to parse the Value
     * @return the parsed Value if exist
    </T> */
    operator fun <T> get(key: String, clazz: Class<T>): Optional<T>

    /**
     * Get a Value of a Type by the key
     * @param key is the Key to get the Value
     * @param type is the Type of the Value
     * @param <T> is the Class to parse the Value
     * @return the parsed Value if exist
    </T> */
    operator fun <T> get(key: String, type: Type): Optional<T>

    /**
     * Get a Value of an specific Class by adding a Gson parser to get the Class
     * @param key is the key to get the Value
     * @param gson is the parser to parse the value
     * @param clazz is the class on wich the value will be parsed
     * @param <T> is the Value class
     * @return the Value if exist
    </T> */
    operator fun <T> get(key: String, gson: Gson, clazz: Class<T>): Optional<T>

    /**
     * Get an Integer and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getInt(key: String, def: Int): Int

    /**
     * Get a Shot and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getShort(key: String, def: Short): Short

    /**
     * Get a Boolean and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getBoolean(key: String, def: Boolean): Boolean

    /**
     * Get a Long and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getLong(key: String, def: Long): Long

    /**
     * Get a Double and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getDouble(key: String, def: Double): Double

    /**
     * Get a Float and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getFloat(key: String, def: Float): Float

    /**
     * Get a String and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getString(key: String, def: String): String

    /**
     * Get a Document and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getDocument(key: String, def: Document): Document

    /**
     * Get a Collection with Documents and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getDocuments(key: String, def: Collection<Document>): Collection<Document>

    /**
     * Get a JsonArray and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getJsonArray(key: String, def: JsonArray): JsonArray

    /**
     * Get a JsonObject and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getJsonObject(key: String, def: JsonObject): JsonObject

    /**
     * Get a Byte Array and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getBinary(key: String, def: ByteArray): ByteArray

    /**
     * Get Properties and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getProperties(key: String, def: Properties): Properties

    /**
     * Get a BigInteger and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getBigInteger(key: String, def: BigInteger): BigInteger

    /**
     * Get a BigDecimal and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getBigDecimal(key: String, def: BigDecimal): BigDecimal

    /**
     * Get a Char and insert a value if the key does not exist
     *
     * @param key is the Key to get the Value
     * @param def is the Default value to set if the key does not exist
     * @return the value if exists otherwise it will return the default value
     */
    fun getChar(key: String, def: Char): Char

    /**
     * @return the current Document as JsonStorage
     */
    fun asJson(): SpecificDocumentStorage

    /**
     * @return the current Document as YamlStorage
     */
    fun asYaml(): SpecificDocumentStorage

    /**
     * Get the Document as SpecificDocumentStorage by an IDocumentStorage instance
     * @param storage is the Storage to get the specific Storage
     * @return the SpecificStorage
     */
    fun storage(storage: DocumentStorage): SpecificDocumentStorage

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

    /**
     * Get all keys of the Document
     * @return all keys in a Collection
     */
    fun getKeys(): MutableCollection<String>

    /**
     * Serialize the current Document to String
     */
    fun toJson(): String {
        return asJson().serializeToString()
    }


}