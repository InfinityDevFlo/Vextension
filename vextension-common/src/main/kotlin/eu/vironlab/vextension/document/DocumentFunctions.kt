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
import eu.vironlab.vextension.document.impl.DefaultDocumentManagement
import eu.vironlab.vextension.document.impl.storage.JsonDocumentStorage
import eu.vironlab.vextension.document.impl.storage.XMLDocumentStorage
import eu.vironlab.vextension.document.impl.storage.YamlDocumentStorage
import eu.vironlab.vextension.document.storage.DocumentStorage

object DocumentFactory {
    @JvmStatic
    fun downloadDocumentDependencies(dependencyLoader: DependencyLoader) {
        dependencyLoader.download("com.google.code.gson:gson:2.8.6")
        dependencyLoader.download("org.yaml:snakeyaml:1.27")
        dependencyLoader.download("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.0.1")
        dependencyLoader.download("com.fasterxml.jackson.core:jackson-databind:2.0.1")
        dependencyLoader.download("com.fasterxml.jackson.core:jackson-core:2.12.3")
        dependencyLoader.download("com.fasterxml.jackson.core:jackson-annotations:2.12.3")
        dependencyLoader.download("org.codehaus.woodstox:stax2-api:4.2.1")
    }

    @JvmStatic
    var documentXmlStorage: DocumentStorage = XMLDocumentStorage()

    @JvmStatic
    var documentJsonStorage: DocumentStorage = JsonDocumentStorage()

    @JvmStatic
    var documentYamlStorage: DocumentStorage = YamlDocumentStorage()

    @JvmStatic
    var documentManagement: DocumentManagement = DefaultDocumentManagement()


    @JvmStatic
    fun createDocument(): Document {
        return documentManagement.createDocument()
    }

    @JvmStatic
    fun createDocument(jsonElement: JsonElement): Document {
        return documentManagement.createDocument(jsonElement)
    }

    @JvmStatic
    fun createDocumentFromJson(json: String): Document {
        return documentJsonStorage.read(json)
    }

    @JvmStatic
    fun createDocument(jsonObject: JsonObject): Document {
        return documentManagement.createDocument(jsonObject)
    }

    @JvmStatic
    fun createDocument(key: String, value: String): Document {
        return documentManagement.createDocument(key, value)
    }

    @JvmStatic
    fun createDocument(key: String, value: Number): Document {
        return documentManagement.createDocument(key, value)
    }

    @JvmStatic
    fun createDocument(key: String, value: Char): Document {
        return documentManagement.createDocument(key, value)
    }

    @JvmStatic
    fun createDocument(key: String, value: Boolean): Document {
        return documentManagement.createDocument(key, value)
    }

    @JvmStatic
    fun createDocument(key: String, value: Any): Document {
        return documentManagement.createDocument(key, value)
    }

    @JvmStatic
    fun <T> createDocument(obj: T): Document {
        return documentManagement.createDocument(obj)
    }
}

fun document(): Document {
    return DocumentFactory.documentManagement.createDocument()
}


fun document(jsonElement: JsonElement): Document {
    return DocumentFactory.documentManagement.createDocument(jsonElement)
}

fun documentFromJson(json: String): Document {
    return DocumentFactory.documentJsonStorage.read(json)
}

fun document(jsonObject: JsonObject): Document {
    return DocumentFactory.documentManagement.createDocument(jsonObject)
}


fun document(key: String, value: String): Document {
    return DocumentFactory.documentManagement.createDocument(key, value)
}


fun document(key: String, value: Number): Document {
    return DocumentFactory.documentManagement.createDocument(key, value)
}


fun document(key: String, value: Char): Document {
    return DocumentFactory.documentManagement.createDocument(key, value)
}


fun document(key: String, value: Boolean): Document {
    return DocumentFactory.documentManagement.createDocument(key, value)
}


fun document(key: String, value: Any): Document {
    return DocumentFactory.documentManagement.createDocument(key, value)
}


fun <T> document(obj: T): Document {
    return DocumentFactory.documentManagement.createDocument(obj)
}
