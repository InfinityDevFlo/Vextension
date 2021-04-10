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
import eu.vironlab.vextension.document.impl.storage.JsonDocumentStorage
import eu.vironlab.vextension.document.impl.storage.XMLDocumentStorage
import eu.vironlab.vextension.document.impl.storage.YamlDocumentStorage
import eu.vironlab.vextension.document.storage.DocumentStorage
import eu.vironlab.vextension.document.impl.DefaultDocumentManagement


var documentXmlStorage: DocumentStorage = XMLDocumentStorage()

var documentJsonStorage: DocumentStorage = JsonDocumentStorage()


var documentYamlStorage: DocumentStorage = YamlDocumentStorage()

var documentManager: DocumentManagement = DefaultDocumentManagement()



fun createDocument(): Document {
    return documentManager.createDocument()
}


fun createDocument(jsonElement: JsonElement): Document {
    return documentManager.createDocument(jsonElement)
}

fun createDocumentFromJson(json: String): Document {
    return documentJsonStorage.read(json)
}

fun createDocument(jsonObject: JsonObject): Document {
    return documentManager.createDocument(jsonObject)
}


fun createDocument(key: String, value: String): Document {
    return documentManager.createDocument(key, value)
}


fun createDocument(key: String, value: Number): Document {
    return documentManager.createDocument(key, value)
}


fun createDocument(key: String, value: Char): Document {
    return documentManager.createDocument(key, value)
}


fun createDocument(key: String, value: Boolean): Document {
    return documentManager.createDocument(key, value)
}


fun createDocument(key: String, value: Any): Document {
    return documentManager.createDocument(key, value)
}


fun <T> createDocument(obj: T): Document {
    return documentManager.createDocument(obj)
}

    
