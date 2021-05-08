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

package eu.vironlab.vextension.document.impl

import com.google.gson.*
import com.google.gson.internal.bind.TypeAdapters
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.document.DocumentFactory.documentJsonStorage
import eu.vironlab.vextension.document.DocumentManagement
import eu.vironlab.vextension.document.storage.DocumentSpecificStorage
import eu.vironlab.vextension.document.storage.DocumentStorage
import eu.vironlab.vextension.document.wrapper.storage.DocumentSpecificStorageWrapper
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.Consumer


internal class DefaultDocumentManagement : DocumentManagement {
    override fun createDocument(): Document {
        return DefaultDocument()
    }

    override fun createDocument(jsonElement: JsonElement): Document {
        return DefaultDocument(jsonElement)
    }

    override fun createDocument(jsonObject: JsonObject): Document {
        return DefaultDocument(jsonObject)
    }

    override fun createDocument(key: String, value: String): Document {
        return DefaultDocument().append(key, value)
    }

    override fun createDocument(key: String, value: Number): Document {
        return DefaultDocument().append(key, value)
    }

    override fun createDocument(key: String, value: Char): Document {
        return DefaultDocument().append(key, value)
    }

    override fun createDocument(key: String, value: Boolean): Document {
        return DefaultDocument().append(key, value)
    }

    override fun createDocument(key: String, value: Any): Document {
        return DefaultDocument().append(key, value)
    }

    override fun <T> createDocument(instance: T): Document {
        return documentJsonStorage.read(instance)
    }


    private class DefaultDocument() : Document {
        internal var jsonObject: JsonObject


        init {
            this.jsonObject = JsonObject()
        }

        constructor(jsonElement: JsonElement) : this() {
            jsonObject = jsonElement.asJsonObject
        }


        override fun toPlainObjects(): Any {
            return asObject(jsonObject)!!
        }

        private fun asObject(element: JsonElement): Any? {
            return if (element.isJsonArray()) {
                val array: MutableCollection<Any?> = ArrayList<Any?>(element.getAsJsonArray().size())
                for (jsonElement in element.getAsJsonArray()) {
                    array.add(asObject(jsonElement))
                }
                array
            } else if (element.isJsonObject()) {
                val map: MutableMap<String, Any> = HashMap<String, Any>(element.getAsJsonObject().size())
                for ((key, value1) in element.getAsJsonObject().entrySet()) {
                    val value = asObject(value1)
                    if (value != null) {
                        map[key] = value
                    }
                }
                map
            } else if (element.isJsonPrimitive) {
                val primitive: JsonPrimitive = element.asJsonPrimitive
                when {
                    primitive.isString -> {
                        primitive.asString
                    }
                    primitive.isNumber -> {
                        primitive.asNumber
                    }
                    primitive.isBoolean -> {
                        primitive.asBoolean
                    }
                    else -> {
                        null
                    }
                }
            } else {
                null
            }
        }

        override fun getKeys(): MutableCollection<String> {
            return this.jsonObject.keySet()
        }

        override fun size(): Int {
            return jsonObject.size()
        }

        override fun <T> toInstance(type: Type): T {
            return GSON.fromJson(this.jsonObject, type)
        }

        override fun isEmpty(): Boolean {
            return false
        }

        override fun clear() {
            getKeys().forEach(Consumer { key: String -> delete(key) })
        }

        override fun delete(key: String) {
            jsonObject.remove(key)
        }

        override fun contains(key: String): Boolean {
            return jsonObject.has(key)
        }

        fun <T> toInstanceOf(clazz: Class<T>?): T {
            return GSON.fromJson(jsonObject, clazz)
        }

        fun <T> toInstanceOf(type: Type?): T {
            return GSON.fromJson(jsonObject, type)
        }

        override fun append(key: String, value: Any): DefaultDocument {
            jsonObject.add(key, GSON.toJsonTree(value))
            return this
        }

        override fun append(key: String, value: Number): DefaultDocument {
            jsonObject.addProperty(key, value)
            return this
        }

        override fun append(key: String, value: Boolean): DefaultDocument {
            jsonObject.addProperty(key, value)
            return this
        }

        override fun append(key: String, value: String): DefaultDocument {
            jsonObject.addProperty(key, value)
            return this
        }

        override fun append(key: String, value: Char): DefaultDocument {
            jsonObject.addProperty(key, value)
            return this
        }

        override fun append(key: String, value: Document): DefaultDocument {
            jsonObject.add(key, (value as DefaultDocument).jsonObject)
            return this
        }

        override fun append(Document: Document): DefaultDocument {
            return append((Document as DefaultDocument).jsonObject)
        }

        override fun append(jsonObject: JsonObject): DefaultDocument {
            for ((key, value) in jsonObject.entrySet()) {
                this.jsonObject.add(key, value)
            }
            return this
        }

        override fun append(key: String, bytes: ByteArray): DefaultDocument {
            return this.append(
                key,
                Base64.getEncoder().encodeToString(bytes)
            )
        }

        override fun append(map: Map<String, Any>): DefaultDocument {
            for ((key, value) in map) {
                this.append(key, value)
            }
            return this
        }

        fun append(inputStream: InputStream?): DefaultDocument {
            try {
                InputStreamReader(inputStream, StandardCharsets.UTF_8).use { reader -> return append(reader) }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return this
        }

        fun append(reader: Reader?): DefaultDocument {
            return append(JsonParser.parseReader(reader).getAsJsonObject())
        }

        override fun getDocument(key: String): Document? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonObject()) {
                DefaultDocument(jsonElement)
            } else {
                null
            }
        }

        override fun getDocuments(key: String): Collection<Document>? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            if (jsonElement.isJsonArray()) {
                val array: JsonArray = jsonElement.getAsJsonArray()
                val Documents: MutableCollection<Document> = ArrayList()
                for (element in array) {
                    if (element.isJsonObject()) {
                        Documents.add(DefaultDocument(element.getAsJsonObject()))
                    }
                }
                return Documents
            }
            return null
        }

        override fun getInt(key: String): Int? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonPrimitive()) {
                jsonElement.getAsInt()
            } else {
                null
            }
        }

        override fun getDouble(key: String): Double? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonPrimitive()) {
                jsonElement.getAsDouble()
            } else {
                return null
            }
        }

        override fun getFloat(key: String): Float? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonPrimitive()) {
                jsonElement.getAsFloat()
            } else {
                return null
            }
        }

        override fun getByte(key: String): Byte? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonPrimitive()) {
                jsonElement.getAsByte()
            } else {
                null
            }
        }

        override fun getShort(key: String): Short? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonPrimitive()) {
                jsonElement.getAsShort()
            } else {
                null
            }
        }

        override fun getLong(key: String): Long? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonPrimitive()) {
                jsonElement.getAsLong()
            } else {
                null
            }
        }

        override fun getBoolean(key: String): Boolean? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonPrimitive()) {
                jsonElement.getAsBoolean()
            } else {
                false
            }
        }

        override fun getString(key: String): String? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonPrimitive()) {
                jsonElement.getAsString()
            } else {
                null
            }
        }

        override fun getChar(key: String): Char? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonPrimitive()) {
                jsonElement.getAsString().toCharArray()[0]
            } else {
                return null
            }
        }

        override fun getBigDecimal(key: String): BigDecimal? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonPrimitive()) {
                jsonElement.getAsBigDecimal()
            } else {
                null
            }
        }

        override fun getBigInteger(key: String): BigInteger? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonPrimitive()) {
                jsonElement.getAsBigInteger()
            } else {
                null
            }
        }

        override fun getJsonArray(key: String): JsonArray? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonArray) {
                jsonElement.asJsonArray
            } else {
                null
            }
        }

        override fun getJsonObject(key: String): JsonObject? {
            if (!contains(key)) {
                return null
            }
            val jsonElement: JsonElement = jsonObject.get(key)
            return if (jsonElement.isJsonObject) {
                jsonElement.asJsonObject
            } else {
                null
            }
        }

        override fun getBinary(key: String): ByteArray? {
            if (!contains(key)) {
                return null
            }
            return Base64.getDecoder().decode(this.getString(key)!!)
        }

        override operator fun <T> get(key: String, clazz: Class<T>): T? {
            return this[key, GSON, clazz]
        }

        override fun <T> get(key: String, type: Type): T? {
            return get(key, GSON, type)
        }

        fun <T> get(key: String, gson: Gson, type: Type): T? {
            return gson.fromJson(get(key), type)
        }

        override operator fun <T> get(key: String, gson: Gson, clazz: Class<T>): T? {
            return gson.fromJson(get(key), clazz)
        }

        override fun get(key: String): JsonElement? {
            if (!contains(key)) {
                return null
            }
            return getJsonObject(key)
        }


        override fun getInt(key: String, def: Int): Int {
            return getInt(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getShort(key: String, def: Short): Short {
            return getShort(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getBoolean(key: String, def: Boolean): Boolean {
            return getBoolean(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getLong(key: String, def: Long): Long {
            return getLong(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getDouble(key: String, def: Double): Double {
            return getDouble(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getFloat(key: String, def: Float): Float {
            return getFloat(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getString(key: String, def: String): String {
            return getString(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getDocument(key: String, def: Document): Document {
            return getDocument(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getDocuments(key: String, def: Collection<Document>): Collection<Document> {
            return getDocuments(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getJsonArray(key: String, def: JsonArray): JsonArray {
            return getJsonArray(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getJsonObject(key: String, def: JsonObject): JsonObject {
            return getJsonObject(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getBinary(key: String, def: ByteArray): ByteArray {
            return getBinary(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun <T> get(key: String, type: Type, def: T): T {
            return get(key, type) ?: run {
                append(key, def as Any)
                def
            }
        }

        override fun <T> get(key: String, clazz: Class<T>, def: T): T {
            return get(key, clazz) ?: run {
                append(key, def as Any)
                def
            }
        }

        override fun getBigInteger(key: String, def: BigInteger): BigInteger {
            return getBigInteger(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getBigDecimal(key: String, def: BigDecimal): BigDecimal {
            return getBigDecimal(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun getChar(key: String, def: Char): Char {
            return getChar(key) ?: run {
                append(key, def)
                def
            }
        }

        override fun storage(storage: DocumentStorage): DocumentSpecificStorage {
            return DocumentSpecificStorageWrapper(this, storage)
        }

        override fun forEach(action: (String) -> Unit) {
            for (element in this.getKeys()) action(element)
        }

        override fun forEach(consumer: Consumer<String>) {
            for (element in this.getKeys()) consumer.accept(element)
        }

        override fun toJsonObject(): JsonObject {
            return jsonObject
        }

        fun toPrettyJson(): String {
            return GSON.toJson(jsonObject)
        }

        fun toJsonString(): String {
            return jsonObject.toString()
        }

        fun toByteArray(): ByteArray {
            return toJsonString().toByteArray(StandardCharsets.UTF_8)
        }

        override fun toString(): String {
            return toJson()
        }


        operator fun iterator(): Iterator<String> {
            return jsonObject.keySet().iterator()
        }

        override fun append(key: String, value: JsonElement): Document {
            return append(key, value.asString)
        }

    }

    class DocumentInstanceCreator : InstanceCreator<Document> {
        override fun createInstance(type: Type): Document {
            return DefaultDocument()
        }
    }

    companion object {

        @JvmStatic
        private val TYPE_ADAPTER: TypeAdapter<DefaultDocument?> = object : TypeAdapter<DefaultDocument?>() {
            @Throws(IOException::class)
            override fun write(jsonWriter: JsonWriter?, document: DefaultDocument?) {
                TypeAdapters.JSON_ELEMENT.write(jsonWriter, document?.jsonObject ?: JsonObject())
            }

            @Throws(IOException::class)
            override fun read(jsonReader: JsonReader): DefaultDocument? {
                val jsonElement: JsonElement = TypeAdapters.JSON_ELEMENT.read(jsonReader)
                return if (jsonElement.isJsonObject) {
                    DefaultDocument(jsonElement)
                } else {
                    null
                }
            }
        }

        @JvmStatic
        var GSON: Gson = GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .registerTypeAdapter(object : TypeToken<Document>() {}.type, DocumentInstanceCreator())
            .registerTypeAdapterFactory(TypeAdapters.newTypeHierarchyFactory(DefaultDocument::class.java, TYPE_ADAPTER))
            .create()
    }

}