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
import eu.vironlab.vextension.dependency.DependencyLoader
import eu.vironlab.vextension.document.storage.DocumentStorage
import eu.vironlab.vextension.document.storage.JsonDocumentStorage
import eu.vironlab.vextension.document.storage.XMLDocumentStorage
import eu.vironlab.vextension.document.storage.YamlDocumentStorage
import java.nio.charset.StandardCharsets
import java.nio.file.Path

/**
 * Create new Documents and store existing ones
 */
object DocumentManagement {

    /**
     * Get the JsonStorage for the Documents
     */
    private val JSON: DocumentStorage = JsonDocumentStorage()

    /**
     * Get the YamlStorage of the Documents
     */
    private val YAML: DocumentStorage = YamlDocumentStorage()

    /**
     * Get the XMLStorage of the Documents
     */
    private val XML: DocumentStorage = XMLDocumentStorage()

    /**
     * Create a new Empty Document
     * @return a new EmptyDocument
     */
    fun newDocument(name: String): DefaultDocument {
        return DefaultDocument(name)
    }

    /**
     * Create a new Document by JsonElement
     * @param jsonElement is the JsonElement to create the Document
     * @return the created Document
     */
    fun newDocument(name: String, jsonElement: JsonElement): DefaultDocument {
        return DefaultDocument(name, jsonElement)
    }

    /**
     * Create a new Document by JsonObject
     * @param jsonObject is the JsonObject, used to create the Document
     * @return the created Document
     */
    fun newDocument(name: String, jsonObject: JsonObject): DefaultDocument {
        return DefaultDocument(name, jsonObject)
    }

    /**
     * Create a new Document and Insert first key and value
     * @param key is the key to insert
     * @param value is the value to insert with the key
     * @return the created Document, wich has inserted the key and value
     */
    fun newDocument(name: String, key: String, value: String): DefaultDocument {
        return DefaultDocument(name).insert(key, value)
    }

    /**
     * Create a new Document and Insert first key and value
     * @param key is the key to insert
     * @param value is the value to insert with the key
     * @return the created Document, wich has inserted the key and value
     */
    fun newDocument(name: String, key: String, value: Number): DefaultDocument {
        return DefaultDocument(name).insert(key, value)
    }

    /**
     * Create a new Document and Insert first key and value
     * @param key is the key to insert
     * @param value is the value to insert with the key
     * @return the created Document, wich has inserted the key and value
     */
    fun newDocument(name: String, key: String, value: Char): DefaultDocument {
        return DefaultDocument(name).insert(key, value)
    }

    /**
     * Create a new Document and Insert first key and value
     * @param key is the key to insert
     * @param value is the value to insert with the key
     * @return the created Document, wich has inserted the key and value
     */
    fun newDocument(name: String, key: String, value: Boolean): DefaultDocument {
        return DefaultDocument(name).insert(key, value)
    }

    /**
     * Create a new Document and Insert first key and value
     * @param key is the key to insert
     * @param value is the value to insert with the key
     * @return the created Document, wich has inserted the key and value
     */
    fun newDocument(name: String, key: String, value: Any): DefaultDocument {
        return DefaultDocument(name).insert(key, value)
    }

    /**
     * Create a new JsonDocument by a Byte Array
     * @param bytes is the array with the data to create the document
     * @return the created Document
     */
    fun newJsonDocument(name: String, bytes: ByteArray): DefaultDocument {
        return newJsonDocument(name, String(bytes, StandardCharsets.UTF_8))
    }

    /**
     * Create a new YamlDocument by a Byte Array
     * @param bytes is the array with the data to create the document
     * @return the created Document
     */
    fun newYamlDocument(name: String, bytes: ByteArray): DefaultDocument {
        return newYamlDocument(name, String(bytes, StandardCharsets.UTF_8))
    }

    /**
     * Create a new JsonDocument by a Path
     * @param path is the Path of the Data for the Document
     * @return the created Document
     */
    fun newJsonDocument(name: String, path: Path): DefaultDocument {
        return jsonStorage().read(name, path)
    }

    /**
     * Create a new YamlDocument by a Path
     * @param path is the Path of the Data for the Document
     * @return the created Document
     */
    fun newYamlDocument(name: String, path: Path): DefaultDocument {
        return yamlStorage().read(name, path)
    }

    /**
     * Create a new JsonDocument by a Json String
     * @param input is the Json String for the Document
     * @return the created Document
     */
    fun newJsonDocument(name: String, input: String): DefaultDocument {
        return jsonStorage().read(name, input)
    }

    /**
     * Create a new YamlDocument by a Yaml String
     * @param input is the Yaml String for the Document
     * @return the created Document
     */
    fun newYamlDocument(name: String, input: String): DefaultDocument {
        return yamlStorage().read(name, input)
    }

    /**
     * @return the XMLStorage
     */
    fun xmlStorage(): DocumentStorage {
        return XML
    }

    /**
     * @return the JsonStorage
     */
    fun jsonStorage(): DocumentStorage {
        return JSON
    }

    /**
     * @return the YamlStorage
     */
    fun yamlStorage(): DocumentStorage {
        return YAML
    }
}

fun initDocumentManagement() {
    DependencyLoader.require("com.google.code.gson:gson:2.8.6")
    DependencyLoader.require("org.yaml:snakeyaml:1.27")
    DependencyLoader.require("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.1")
    DependencyLoader.require("com.fasterxml.jackson.core:jackson-databind:2.12.1")
    DependencyLoader.require("com.fasterxml.jackson.core:jackson-core:2.12.1")
    DependencyLoader.require("com.fasterxml.jackson.core:jackson-annotations:2.12.1")
    DependencyLoader.require("org.codehaus.woodstox:stax2-api:4.2.1")

}