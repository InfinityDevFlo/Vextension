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
import eu.vironlab.vextension.document.storage.DocumentStorage
import eu.vironlab.vextension.document.storage.SpecificDocumentStorage
import java.io.File
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.function.Consumer


class ConfigDocument(val file: File) : Document {

    private var wrapped: Document

    init {
        this.wrapped = DocumentManagement.newDocument()
    }

    fun loadConfig() {
        this.wrapped = DocumentManagement.newJsonDocument(file.toPath())
    }

    fun saveConfig() {
        wrapped.storage(DocumentManagement.jsonStorage()).write(file)
    }

    override fun toPlainObjects(): Any {
        return wrapped.toPlainObjects()
    }

    override fun toJsonObject(): JsonObject {
        return wrapped.toJsonObject()
    }

    override fun insert(key: String, value: Any): Document {
        return wrapped.insert(key, value)
    }

    override fun insert(key: String, value: Number): Document {
        return wrapped.insert(key, value)
    }

    override fun insert(key: String, value: Boolean): Document {
        return wrapped.insert(key, value)
    }

    override fun insert(key: String, value: String): Document {
        return wrapped.insert(key, value)
    }

    override fun insert(key: String, value: Char): Document {
        return wrapped.insert(key, value)
    }

    override fun insert(key: String, value: Document): Document {
        return wrapped.insert(key, value)
    }

    override fun insert(Document: Document): Document {
        return wrapped.insert(wrapped)
    }

    override fun insert(jsonObject: JsonObject): Document {
        return wrapped.insert(jsonObject)
    }

    override fun insert(properties: Properties): Document {
        return wrapped.insert(properties)
    }

    override fun insert(key: String, properties: Properties): Document {
        return wrapped.insert(key, properties)
    }

    override fun insert(key: String, bytes: ByteArray): Document {
        return wrapped.insert(key, bytes)
    }

    override fun insert(map: Map<String, Any>): Document {
        return wrapped.insert(map)
    }

    override fun insert(key: String, value: JsonElement): Document {
        return wrapped.insert(key, value)
    }

    override fun getDocument(key: String): Optional<Document> {
        return wrapped.getDocument(key)
    }

    override fun getDocument(key: String, def: Document): Document {
        return wrapped.getDocument(key, def)
    }

    override fun getDocuments(key: String): Optional<Collection<Document>> {
        return wrapped.getDocuments(key)
    }

    override fun getDocuments(key: String, def: Collection<Document>): Collection<Document> {
        return wrapped.getDocuments(key, def)
    }

    override fun getInt(key: String): Optional<Int> {
        return wrapped.getInt(key)
    }

    override fun getInt(key: String, def: Int): Int {
        return wrapped.getInt(key, def)
    }

    override fun getDouble(key: String): Optional<Double> {
        return wrapped.getDouble(key)
    }

    override fun getDouble(key: String, def: Double): Double {
        return wrapped.getDouble(key, def)
    }

    override fun getFloat(key: String): Optional<Float> {
        return wrapped.getFloat(key)
    }

    override fun getFloat(key: String, def: Float): Float {
        return wrapped.getFloat(key, def)
    }

    override fun getByte(key: String): Optional<Byte> {
        return wrapped.getByte(key)
    }

    override fun getShort(key: String): Optional<Short> {
        return wrapped.getShort(key)
    }

    override fun getShort(key: String, def: Short): Short {
        return wrapped.getShort(key, def)
    }

    override fun getLong(key: String): Optional<Long> {
        return wrapped.getLong(key)
    }

    override fun getLong(key: String, def: Long): Long {
        return wrapped.getLong(key, def)
    }

    override fun getBoolean(key: String): Optional<Boolean> {
        return wrapped.getBoolean(key)
    }

    override fun getBoolean(key: String, def: Boolean): Boolean {
        return wrapped.getBoolean(key, def)
    }

    override fun getString(key: String): Optional<String> {
        return wrapped.getString(key)
    }

    override fun getString(key: String, def: String): String {
        return wrapped.getString(key, def)
    }

    override fun getChar(key: String): Optional<Char> {
        return wrapped.getChar(key)
    }

    override fun getChar(key: String, def: Char): Char {
        return wrapped.getChar(key, def)
    }

    override fun getBigDecimal(key: String): Optional<BigDecimal> {
        return wrapped.getBigDecimal(key)
    }

    override fun getBigDecimal(key: String, def: BigDecimal): BigDecimal {
        return wrapped.getBigDecimal(key, def)
    }

    override fun getBigInteger(key: String): Optional<BigInteger> {
        return wrapped.getBigInteger(key)
    }

    override fun getBigInteger(key: String, def: BigInteger): BigInteger {
        return wrapped.getBigInteger(key, def)
    }

    override fun getJsonArray(key: String): Optional<JsonArray> {
        return wrapped.getJsonArray(key)
    }

    override fun getJsonArray(key: String, def: JsonArray): JsonArray {
        return wrapped.getJsonArray(key, def)
    }

    override fun getJsonObject(key: String): Optional<JsonObject> {
        return wrapped.getJsonObject(key)
    }

    override fun getJsonObject(key: String, def: JsonObject): JsonObject {
        return wrapped.getJsonObject(key, def)
    }

    override fun getProperties(key: String): Optional<Properties> {
        return wrapped.getProperties(key)
    }

    override fun getProperties(key: String, def: Properties): Properties {
        return wrapped.getProperties(key, def)
    }

    override fun getBinary(key: String): Optional<ByteArray> {
        return wrapped.getBinary(key)
    }

    override fun getBinary(key: String, def: ByteArray): ByteArray {
        return wrapped.getBinary(key, def)
    }

    override fun get(key: String): Optional<JsonElement> {
        return wrapped.get(key)
    }

    override fun <T> get(key: String, clazz: Class<T>): Optional<T> {
        return wrapped.get(key, clazz)
    }

    override fun <T> get(key: String, type: Type): Optional<T> {
        return wrapped.get(key, type)
    }

    override fun <T> get(key: String, gson: Gson, clazz: Class<T>): Optional<T> {
        return wrapped.get(key, gson, clazz)
    }

    override fun asJson(): SpecificDocumentStorage {
        return wrapped.asJson()
    }

    override fun asYaml(): SpecificDocumentStorage {
        return wrapped.asYaml()
    }

    override fun storage(storage: DocumentStorage): SpecificDocumentStorage {
        return wrapped.storage(storage)
    }

    override fun forEach(action: (String) -> Unit) {
        return wrapped.forEach(action)
    }

    override fun forEach(consumer: Consumer<String>) {
        return wrapped.forEach(consumer)
    }

    override fun contains(key: String): Boolean {
        return wrapped.contains(key)
    }

    override fun delete(key: String) {
        return wrapped.delete(key)
    }

    override fun clear() {
        return wrapped.clear()
    }

    override fun size(): Int {
        return wrapped.size()
    }

    override fun getKeys(): MutableCollection<String> {
        return wrapped.getKeys()
    }

    override fun init(document: Document) {
        wrapped = document
    }

    override fun toDocument(): Document {
        return wrapped
    }
}