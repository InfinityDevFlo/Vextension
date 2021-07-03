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

package eu.vironlab.vextension.document.storage

import eu.vironlab.vextension.document.Document
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

interface DocumentStorage {

    /**
     * Get a Document by an InputStream
     *
     * @param inputStream is the Stream to collect the Data for the Document
     *
     * @return a new Document
     */
    fun read(inputStream: InputStream): Document {
        InputStreamReader(inputStream, StandardCharsets.UTF_8).use { inputStreamReader ->
            return this.read(
                inputStreamReader
            )
        }
    }

    fun <T> read(instance: T): Document

    /**
     * Get a Document by a Path of a File
     *
     * @param path is the Path of the File
     *
     * @return a new Document
     */
    fun read(path: Path): Document {
        Files.newInputStream(path).use { stream -> return this.read(stream) }
    }

    fun read(file: File): Document {
        return this.read(file.toPath())
    }

    fun read(bytes: ByteArray): Document {
        return this.read(ByteArrayInputStream(bytes))
    }

    fun read(input: String): Document {
        return this.read(StringReader(input))
    }

    fun read(reader: Reader): Document

    fun <T> write(document: Document, clazz: Class<T>): T

    fun write(document: Document, outputStream: OutputStream) {
        try {
            OutputStreamWriter(outputStream, StandardCharsets.UTF_8).use { outputStreamWriter ->
                this.write(
                    document,
                    outputStreamWriter
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun write(document: Document, file: File) {
        this.write(document, file.toPath())
    }

    fun write(document: Document, path: Path) {
        val parent = path.parent
        try {
            if (parent != null) {
                Files.createDirectories(parent)
            }
            Files.newOutputStream(path).use { stream -> this.write(document, stream) }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }

    fun toString(document: Document): String {
        try {
            StringWriter().use { writer ->
                this.write(document, writer)
                return writer.toString()
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
        return ""
    }

    fun write(document: Document, writer: Writer)
}