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
 *   Creation: Donnerstag 22 Juli 2021 20:57:50<p>
 *<p>
 * <p>
 * Contact:<p>
 * <p>
 * Discordserver:   https://discord.gg/wvcX92VyEH<p>
 * Website:         https://vironlab.eu/ <p>
 * Mail:            contact@vironlab.eu<p>
 * <p>
 */
@file:JvmName("HasteUtils")

package eu.vironlab.vextension.extension;

import eu.vironlab.vextension.document.documentFromJson
import java.io.InputStream
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

val httpClient: HttpClient = HttpClient.newHttpClient()

fun String.haste(hasteServer: String = "https://just-paste.it"): HasteResult {
    val request =
        HttpRequest.newBuilder(URI("$hasteServer/documents")).POST(HttpRequest.BodyPublishers.ofString(this)).build()
    val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    val doc = documentFromJson(response.body())
    val key = doc.getString("key") ?: throw IllegalStateException("Cannot parse Key from Haste Result")
    return HasteResult(URL("$hasteServer/$key"), URL("$hasteServer/raw/$key"), key)
}

fun InputStream.haste(hasteServer: String = "https://just-paste.it"): HasteResult =
    this.bufferedReader().readText().haste(hasteServer)

data class HasteResult(val url: URL, val rawUrl: URL, val key: String)