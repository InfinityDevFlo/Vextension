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

package eu.vironlab.vextension.rest

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.document.DocumentManagement
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class RestClient(val agent: String) {

    fun getJsonArray(url: String): Optional<JsonArray> {
        return getJsonArray(url, mutableMapOf())
    }

    fun getDocument(url: String): Optional<Document> {
        return getDocument(url, mutableMapOf())
    }

    fun <T> getClassInstance(url: String, clazz: Class<T>): Optional<T> {
        return getClassInstance(url, clazz, mutableMapOf(), Gson())
    }

    fun <T> getClassInstance(url: String, clazz: Class<T>, gson: Gson): Optional<T> {
        return getClassInstance(url, clazz, mutableMapOf(), gson)
    }

    fun <T> getClassInstance(url: String, clazz: Class<T>, properties: Map<String, String>): Optional<T> {
        return getClassInstance(url, clazz, properties, Gson())
    }

    fun getJsonArray(urlStr: String, properties: Map<String, String>): Optional<JsonArray> {
        try {
            val url: URL = URL(urlStr)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = HttpMethod.GET.toString()
            connection.setRequestProperty("User-Agent", agent)
            if (!properties.isEmpty()) {
                properties.forEach { k, v ->
                    connection.setRequestProperty(k, v)
                }
            }
            connection.connect()
            val input = BufferedReader(InputStreamReader(connection.getInputStream()))
            val result: JsonArray = JsonParser.parseReader(input).asJsonArray
            connection.disconnect()
            return Optional.of(result)
        } catch (e: Exception) {
            return Optional.ofNullable(null)
        }
    }

    fun getDocument(urlStr: String, properties: Map<String, String>): Optional<Document> {
        try {
            val url = URL(urlStr)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = HttpMethod.GET.toString()
            connection.setRequestProperty("User-Agent", agent)
            if (!properties.isEmpty()) {
                properties.forEach { k, v ->
                    connection.setRequestProperty(k, v)
                }
            }
            connection.connect()
            val input = BufferedReader(InputStreamReader(connection.getInputStream()))
            val document = DocumentManagement.jsonStorage().read(urlStr, input)
            connection.disconnect()
            return Optional.of(document)
        } catch (e: Exception) {
            return Optional.ofNullable(null)
        }
    }

    fun <T> getClassInstance(
        urlStr: String,
        clazz: Class<T>,
        properties: Map<String, String>,
        gson: Gson
    ): Optional<T> {
        try {
            val url = URL(urlStr)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = HttpMethod.GET.toString()
            connection.setRequestProperty("User-Agent", agent)
            if (!properties.isEmpty()) {
                properties.forEach { k, v ->
                    connection.setRequestProperty(k, v)
                }
            }
            connection.connect()
            var content: StringBuilder
            try {
                BufferedReader(InputStreamReader(connection.inputStream)).use { input ->
                    var line: String?
                    content = StringBuilder()
                    while (input.readLine().also { line = it } != null) {
                        content.append(line)
                    }
                }
            } finally {
                connection.disconnect()
            }
            return Optional.of(gson.fromJson(content.toString(), clazz))
        } catch (e: Exception) {
            return Optional.ofNullable(null)
        }
    }


}