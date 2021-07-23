/**
 * Copyright © 2020 | vironlab.eu | Licensed under the GNU General Public license Version 3<p>
 * <p>
 * ___    _______                        ______         ______  <p>
 * __ |  / /___(_)______________ _______ ___  / ______ ____  /_ <p>
 * __ | / / __  / __  ___/_  __ \__  __ \__  /  _  __ `/__  __ \<p>
 * __ |/ /  _  /  _  /    / /_/ /_  / / /_  /___/ /_/ / _  /_/ /<p>
 * _____/   /_/   /_/     \____/ /_/ /_/ /_____/\__,_/  /_.___/ <p>
 * <p>
 * ____  _______     _______ _     ___  ____  __  __ _____ _   _ _____ <p>
 * |  _ \| ____\ \   / / ____| |   / _ \|  _ \|  \/  | ____| \ | |_   _|<p>
 * | | | |  _|  \ \ / /|  _| | |  | | | | |_) | |\/| |  _| |  \| | | |  <p>
 * | |_| | |___  \ V / | |___| |__| |_| |  __/| |  | | |___| |\  | | |  <p>
 * |____/|_____|  \_/  |_____|_____\___/|_|   |_|  |_|_____|_| \_| |_|  <p>
 * <p>
 * <p>
 * This program is free software: you can redistribute it and/or modify<p>
 * it under the terms of the GNU General Public License as published by<p>
 * the Free Software Foundation, either version 3 of the License, or<p>
 * (at your option) any later version.<p>
 * <p>
 * This program is distributed in the hope that it will be useful,<p>
 * but WITHOUT ANY WARRANTY; without even the implied warranty of<p>
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<p>
 * GNU General Public License for more details.<p>
 * <p>
 * You should have received a copy of the GNU General Public License<p>
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.<p>
 *<p>
 *   Creation: Freitag 23 Juli 2021 10:40:01<p>
 *<p>
 * <p>
 * Contact:<p>
 * <p>
 * Discordserver:   https://discord.gg/wvcX92VyEH<p>
 * Website:         https://vironlab.eu/ <p>
 * Mail:            contact@vironlab.eu<p>
 * <p>
 */
@file:JvmName("URLUtil")

package eu.vironlab.vextension.extension;

import com.google.gson.JsonArray
import com.google.gson.JsonParser
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.document.DocumentFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

var AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"

fun URL.yaml(properties: Map<String, String> = mapOf()): Document? = DocumentFactory.instance.documentYamlStorage.read(this.content(properties) ?: run { return null })


fun URL.json(properties: Map<String, String> = mapOf()): Document? = DocumentFactory.instance.documentJsonStorage.read(this.content(properties) ?: run { return null })

fun URL.xml(properties: Map<String, String> = mapOf()): Document? = DocumentFactory.instance.documentXmlStorage.read(this.content(properties) ?: run { return null })

fun URL.jsonArray(properties: Map<String, String> = mapOf()): JsonArray? =
    JsonParser.parseString(this.content(properties) ?: run { return null }).asJsonArray

fun URL.content(properties: Map<String, String> = mapOf()): String? {
    try {
        val connection: HttpURLConnection = this.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("User-Agent", AGENT)
        if (properties.isNotEmpty()) {
            properties.forEach { (k, v) ->
                connection.setRequestProperty(k, v)
            }
        }
        connection.connect()
        val content: StringBuilder = StringBuilder()
        try {
            BufferedReader(InputStreamReader(connection.inputStream)).use { input ->
                var line: String?
                while (input.readLine().also { line = it } != null) {
                    content.append(line)
                }
            }
        } finally {
            connection.disconnect()
        }
        return content.toString()
    } catch (e: Exception) {
        return null
    }
}

fun URL.postJson(json: Document, headers: Map<String, String> = mapOf()): Boolean {
    try {
        val connection: HttpURLConnection = this.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("User-Agent", AGENT)
        if (headers.isNotEmpty()) {
            headers.forEach { (k, v) ->
                connection.setRequestProperty(k, v)
            }
        }
        connection.connect()
        OutputStreamWriter(connection.outputStream, "UTF-8").let {
            it.write(json.toJson())
            it.flush()
            it.close()
        }
        connection.outputStream.close()
        connection.outputStream.flush()
        connection.outputStream.close()
        connection.disconnect()
        return true
    } catch (e: Exception) {
        return false
    }
}

fun URL.status(method: String = "GET"): Int {
    val connection: HttpURLConnection = this.openConnection() as HttpURLConnection
    connection.requestMethod = method
    connection.setRequestProperty("User-Agent", AGENT)
    connection.connect()
    val rs = connection.responseCode
    connection.disconnect()
    return rs
}