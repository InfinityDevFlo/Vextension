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
    fun newDocument(): DefaultDocument {
        return DefaultDocument()
    }

    /**
     * Create a new Document by JsonElement
     * @param jsonElement is the JsonElement to create the Document
     * @return the created Document
     */
    fun newDocument(jsonElement: JsonElement): DefaultDocument {
        return DefaultDocument(jsonElement)
    }

    /**
     * Create a new Document by JsonObject
     * @param jsonObject is the JsonObject, used to create the Document
     * @return the created Document
     */
    fun newDocument(jsonObject: JsonObject): DefaultDocument {
        return DefaultDocument(jsonObject)
    }

    /**
     * Create a new Document and Insert first key and value
     * @param key is the key to insert
     * @param value is the value to insert with the key
     * @return the created Document, wich has inserted the key and value
     */
    fun newDocument(key: String, value: String): DefaultDocument {
        return DefaultDocument().insert(key, value)
    }

    /**
     * Create a new Document and Insert first key and value
     * @param key is the key to insert
     * @param value is the value to insert with the key
     * @return the created Document, wich has inserted the key and value
     */
    fun newDocument(key: String, value: Number): DefaultDocument {
        return DefaultDocument().insert(key, value)
    }

    /**
     * Create a new Document and Insert first key and value
     * @param key is the key to insert
     * @param value is the value to insert with the key
     * @return the created Document, wich has inserted the key and value
     */
    fun newDocument(key: String, value: Char): DefaultDocument {
        return DefaultDocument().insert(key, value)
    }

    /**
     * Create a new Document and Insert first key and value
     * @param key is the key to insert
     * @param value is the value to insert with the key
     * @return the created Document, wich has inserted the key and value
     */
    fun newDocument(key: String, value: Boolean): DefaultDocument {
        return DefaultDocument().insert(key, value)
    }

    /**
     * Create a new Document and Insert first key and value
     * @param key is the key to insert
     * @param value is the value to insert with the key
     * @return the created Document, wich has inserted the key and value
     */
    fun newDocument(key: String, value: Any): DefaultDocument {
        return DefaultDocument().insert(key, value)
    }

    /**
     * Create a new JsonDocument by a Byte Array
     * @param bytes is the array with the data to create the document
     * @return the created Document
     */
    fun newJsonDocument(bytes: ByteArray): DefaultDocument {
        return newJsonDocument(String(bytes, StandardCharsets.UTF_8))
    }

    /**
     * Create a new YamlDocument by a Byte Array
     * @param bytes is the array with the data to create the document
     * @return the created Document
     */
    fun newYamlDocument(bytes: ByteArray): DefaultDocument {
        return newYamlDocument(String(bytes, StandardCharsets.UTF_8))
    }

    /**
     * Create a new JsonDocument by a Path
     * @param path is the Path of the Data for the Document
     * @return the created Document
     */
    fun newJsonDocument(path: Path): DefaultDocument {
        return jsonStorage().read(path)
    }

    /**
     * Create a new YamlDocument by a Path
     * @param path is the Path of the Data for the Document
     * @return the created Document
     */
    fun newYamlDocument(path: Path): DefaultDocument {
        return yamlStorage().read(path)
    }

    /**
     * Create a new JsonDocument by a Json String
     * @param input is the Json String for the Document
     * @return the created Document
     */
    fun newJsonDocument(input: String): DefaultDocument {
        return jsonStorage().read(input)
    }

    /**
     * Create a new YamlDocument by a Yaml String
     * @param input is the Yaml String for the Document
     * @return the created Document
     */
    fun newYamlDocument(input: String): DefaultDocument {
        return yamlStorage().read(input)
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

fun createDocument(): Document {
    return DocumentManagement.newDocument()
}

fun createDocument(obj: Any): Document {
    return DocumentManagement.newJsonDocument(DefaultDocument.GSON.toJson(obj))
}

fun initDocumentManagement(dependencyLoader: DependencyLoader) {
    dependencyLoader.download("com.google.code.gson:gson:2.8.6")
    dependencyLoader.download("org.yaml:snakeyaml:1.27")
    dependencyLoader.download("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.1")
    dependencyLoader.download("com.fasterxml.jackson.core:jackson-databind:2.12.1")
    dependencyLoader.download("com.fasterxml.jackson.core:jackson-core:2.12.1")
    dependencyLoader.download("com.fasterxml.jackson.core:jackson-annotations:2.12.1")
    dependencyLoader.download("org.codehaus.woodstox:stax2-api:4.2.1")
}