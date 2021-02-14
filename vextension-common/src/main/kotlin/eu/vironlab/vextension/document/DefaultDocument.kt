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

import com.google.gson.*
import com.google.gson.internal.bind.TypeAdapters
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import eu.vironlab.vextension.document.storage.DocumentStorage
import eu.vironlab.vextension.document.storage.SpecificDocumentStorage
import eu.vironlab.vextension.document.storage.WrappedSpecificDocumentStorage
import eu.vironlab.vextension.lang.Nameable
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class DefaultDocument(override val name: String) : Document, Nameable {
    private var jsonObject: JsonObject


    init {
        this.jsonObject = JsonObject()
    }

    constructor(name: String, jsonElement: JsonElement) : this(name) {
        jsonObject = jsonElement.getAsJsonObject()
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
        } else if (element.isJsonPrimitive()) {
            val primitive: JsonPrimitive = element.getAsJsonPrimitive()
            if (primitive.isString()) {
                primitive.getAsString()
            } else if (primitive.isNumber()) {
                primitive.getAsNumber()
            } else if (primitive.isBoolean()) {
                primitive.getAsBoolean()
            } else {
                null
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
        return key != null && jsonObject.has(key)
    }

    fun <T> toInstanceOf(clazz: Class<T>?): T {
        return GSON.fromJson(jsonObject, clazz)
    }

    fun <T> toInstanceOf(type: Type?): T {
        return GSON.fromJson(jsonObject, type)
    }

    override fun insert(key: String, value: Any): DefaultDocument {
        if (key == null || value == null) {
            return this
        }
        jsonObject.add(key, GSON.toJsonTree(value))
        return this
    }

    override fun insert(key: String, value: Number): DefaultDocument {
        if (key == null || value == null) {
            return this
        }
        jsonObject.addProperty(key, value)
        return this
    }

    override fun insert(key: String, value: Boolean): DefaultDocument {
        if (key == null || value == null) {
            return this
        }
        jsonObject.addProperty(key, value)
        return this
    }

    override fun insert(key: String, value: String): DefaultDocument {
        if (key == null || value == null) {
            return this
        }
        jsonObject.addProperty(key, value)
        return this
    }

    override fun insert(key: String, value: Char): DefaultDocument {
        if (key == null || value == null) {
            return this
        }
        jsonObject.addProperty(key, value)
        return this
    }

    override fun insert(key: String, value: Document): DefaultDocument {
        if (key == null || value == null) {
            return this
        }
        jsonObject.add(key, (value as DefaultDocument).jsonObject)
        return this
    }

    override fun insert(Document: Document): DefaultDocument {
        return if (Document == null) {
            this
        } else {
            insert((Document as DefaultDocument).jsonObject)
        }
    }

    override fun insert(jsonObject: JsonObject): DefaultDocument {
        if (jsonObject == null) {
            return this
        }
        for ((key, value) in jsonObject.entrySet()) {
            this.jsonObject.add(key, value)
        }
        return this
    }

    override fun insert(key: String, bytes: ByteArray): DefaultDocument {
        return if (key == null || bytes == null) {
            this
        } else this.insert(
            key,
            Base64.getEncoder().encodeToString(bytes)
        )
    }

    override fun insert(map: Map<String, Any>): DefaultDocument {
        if (map == null) {
            return this
        }
        for ((key, value) in map) {
            this.insert(key, value)
        }
        return this
    }

    fun insert(inputStream: InputStream?): DefaultDocument {
        try {
            InputStreamReader(inputStream, StandardCharsets.UTF_8).use { reader -> return insert(reader) }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return this
    }

    fun insert(reader: Reader?): DefaultDocument {
        return insert(JsonParser.parseReader(reader).getAsJsonObject())
    }

    override fun getDocument(key: String): Optional<Document> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonObject()) {
            Optional.of(DefaultDocument(key, jsonElement))
        } else {
            Optional.ofNullable(null)
        }
    }

    override fun getDocuments(key: String): Optional<Collection<Document>> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        if (jsonElement.isJsonArray()) {
            val array: JsonArray = jsonElement.getAsJsonArray()
            val Documents: MutableCollection<Document> = ArrayList()
            for (element in array) {
                if (element.isJsonObject()) {
                    Documents.add(DefaultDocument(key, element.getAsJsonObject()))
                }
            }
            return Optional.of(Documents)
        }
        return Optional.ofNullable(null)
    }

    override fun getInt(key: String): Optional<Int> {
        if (!contains(key)) {
            return Optional.ofNullable(0)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonPrimitive()) {
            Optional.ofNullable(jsonElement.getAsInt())
        } else {
            return Optional.ofNullable(0)
        }
    }

    override fun getDouble(key: String): Optional<Double> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonPrimitive()) {
            Optional.of(jsonElement.getAsDouble())
        } else {
            return Optional.ofNullable(null)
        }
    }

    override fun getFloat(key: String): Optional<Float> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonPrimitive()) {
            Optional.ofNullable(jsonElement.getAsFloat())
        } else {
            return Optional.ofNullable(null)
        }
    }

    override fun getByte(key: String): Optional<Byte> {
        if (!contains(key)) {
            return Optional.ofNullable(0)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonPrimitive()) {
            Optional.ofNullable(jsonElement.getAsByte())
        } else {
            Optional.ofNullable(0)
        }
    }

    override fun getShort(key: String): Optional<Short> {
        if (!contains(key)) {
            return Optional.ofNullable(0)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonPrimitive()) {
            Optional.ofNullable(jsonElement.getAsShort())
        } else {
            Optional.ofNullable(0)
        }
    }

    override fun getLong(key: String): Optional<Long> {
        if (!contains(key)) {
            return Optional.ofNullable(0)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonPrimitive()) {
            Optional.ofNullable(jsonElement.getAsLong())
        } else {
            Optional.ofNullable(0)
        }
    }

    override fun getBoolean(key: String): Optional<Boolean> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonPrimitive()) {
            Optional.ofNullable(jsonElement.getAsBoolean())
        } else {
            Optional.ofNullable(false)
        }
    }

    override fun getString(key: String): Optional<String>{
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonPrimitive()) {
            Optional.ofNullable(jsonElement.getAsString())
        } else {
            Optional.ofNullable(null)
        }
    }

    override fun getChar(key: String): Optional<Char> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonPrimitive()) {
            Optional.ofNullable(jsonElement.getAsString().toCharArray()[0])
        } else {
            return Optional.ofNullable(null)
        }
    }

    override fun getBigDecimal(key: String): Optional<BigDecimal> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonPrimitive()) {
            Optional.ofNullable(jsonElement.getAsBigDecimal())
        } else {
            Optional.ofNullable(null)
        }
    }

    override fun getBigInteger(key: String): Optional<BigInteger> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonPrimitive()) {
            Optional.ofNullable(jsonElement.getAsBigInteger())
        } else {
            Optional.ofNullable(null)
        }
    }

    override fun getJsonArray(key: String): Optional<JsonArray> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonArray()) {
            Optional.ofNullable(jsonElement.getAsJsonArray())
        } else {
            Optional.ofNullable(null)
        }
    }

    override fun getJsonObject(key: String): Optional<JsonObject> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val jsonElement: JsonElement = jsonObject.get(key)
        return if (jsonElement.isJsonObject()) {
            Optional.ofNullable(jsonElement.getAsJsonObject())
        } else {
            Optional.ofNullable(null)
        }
    }

    override fun getBinary(key: String): Optional<ByteArray> {
        if (!contains(key)) {
            Optional.ofNullable(null)
        }
        return Optional.ofNullable(Base64.getDecoder().decode(this.getString(key).get()))
    }

    override fun <T> get(key: String, clazz: Class<T>): Optional<T> {
        return this[key, GSON, clazz]
    }

    override fun <T> get(key: String, type: Type): Optional<T> {
        return get(key, GSON, type)
    }

    fun <T> get(key: String, gson: Gson, type: Type): Optional<T> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val jsonElement = get(key)
        return if (!jsonElement.isPresent()) {
            Optional.ofNullable(null)
        } else {
            Optional.ofNullable(gson.fromJson(jsonElement.get(), type))
        }
    }

    override operator fun <T> get(key: String, gson: Gson, clazz: Class<T>): Optional<T> {
        val jsonElement = get(key)
        return if (jsonElement == null) {
            Optional.ofNullable(null)
        } else {
            Optional.ofNullable(gson.fromJson(jsonElement.get(), clazz))
        }
    }

    override fun get(key: String): Optional<JsonElement> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        return Optional.ofNullable(getJsonObject(key).get().asJsonObject)
    }


    override fun getInt(key: String, def: Int): Int {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getInt(key).get()
    }

    override fun getShort(key: String, def: Short): Short {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getShort(key).get()
    }

    override fun getBoolean(key: String, def: Boolean): Boolean {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getBoolean(key).get()
    }

    override fun getLong(key: String, def: Long): Long {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getLong(key).get()
    }

    override fun getDouble(key: String, def: Double): Double {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getDouble(key).get()
    }

    override fun getFloat(key: String, def: Float): Float {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getFloat(key).get()
    }

    override fun getString(key: String, def: String): String {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getString(key).get()
    }

    override fun getDocument(key: String, def: Document): Document {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getDocument(key).get()
    }

    override fun getDocuments(key: String, def: Collection<Document>): Collection<Document> {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getDocuments(key).get()
    }

    override fun getJsonArray(key: String, def: JsonArray): JsonArray {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getJsonArray(key).get()
    }

    override fun getJsonObject(key: String, def: JsonObject): JsonObject {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getJsonObject(key).get()
    }

    override fun getBinary(key: String, def: ByteArray): ByteArray {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getBinary(key).get()
    }

    fun <T> get(key: String, type: Type, def: T): T {
        if (!this.contains(key)) {
            this.insert(key, def as Any)
        }
        return get<T>(key, type).get()
    }

    fun <T> get(key: String, clazz: Class<T>, def: T): T {
        if (!this.contains(key)) {
            this.insert(key, def as Any)
        }
        return this[key, clazz].get()
    }

    override fun getBigInteger(key: String, def: BigInteger): BigInteger {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getBigInteger(key).get()
    }

    override fun getBigDecimal(key: String, def: BigDecimal): BigDecimal {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getBigDecimal(key).get()
    }

    override fun getChar(key: String, def: Char): Char {
        if (!this.contains(key)) {
            this.insert(key, def)
        }
        return this.getChar(key).get()
    }

    override fun asJson(): SpecificDocumentStorage {
        return storage(DocumentManagement.jsonStorage())
    }

    override fun asYaml(): SpecificDocumentStorage {
        return storage(DocumentManagement.yamlStorage())
    }

    override fun storage(storage: DocumentStorage): SpecificDocumentStorage {
        return WrappedSpecificDocumentStorage(this, storage)
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

    override fun insert(key: String, value: JsonElement): Document {
        return insert(key, value.getAsString())
    }

    companion object {
        val TYPE_ADAPTER: TypeAdapter<DefaultDocument?> = object : TypeAdapter<DefaultDocument?>() {
            @Throws(IOException::class)
            override fun write(jsonWriter: JsonWriter?, document: DefaultDocument?) {
                TypeAdapters.JSON_ELEMENT.write(jsonWriter, document?.jsonObject ?: JsonObject())
            }

            @Throws(IOException::class)
            override fun read(jsonReader: JsonReader): DefaultDocument? {
                val jsonElement: JsonElement = TypeAdapters.JSON_ELEMENT.read(jsonReader)
                return if (jsonElement != null && jsonElement.isJsonObject()) {
                    DefaultDocument(jsonElement.hashCode().toString(), jsonElement)
                } else {
                    null
                }
            }
        }
        var GSON: Gson = GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(TypeAdapters.newTypeHierarchyFactory(DefaultDocument::class.java, TYPE_ADAPTER))
            .create()
    }
}