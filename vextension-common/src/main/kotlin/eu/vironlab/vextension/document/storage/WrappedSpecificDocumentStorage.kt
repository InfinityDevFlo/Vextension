
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
import java.io.File
import java.io.OutputStream
import java.io.Writer
import java.nio.file.Path
import java.util.*

class WrappedSpecificDocumentStorage(Document: Document, storage: DocumentStorage) : SpecificDocumentStorage {
    private val storage: DocumentStorage
    private val Document: Document
    override fun document(): Document {
        return Document
    }

    override fun write(outputStream: OutputStream): Optional<SpecificDocumentStorage> {
        storage.write(Document, outputStream)
        return Optional.of(this)
    }

    override fun write(file: File): Optional<SpecificDocumentStorage>  {
        storage.write(Document, file)
        return Optional.of(this)
    }

    override fun write(path: Path): Optional<SpecificDocumentStorage>   {
        storage.write(Document, path)
        return Optional.of(this)
    }

    override fun serializeToString(): String {
        return storage.toString(Document)
    }

    override fun write(writer: Writer): Optional<SpecificDocumentStorage>  {
        storage.write(Document, writer)
        return Optional.of(this)
    }

    init {
        this.Document = Document
        this.storage = storage
    }
}