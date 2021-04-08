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

package eu.vironlab.vextension.database

import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.lang.Nameable
import java.util.*
import java.util.concurrent.CompletableFuture

interface Database : Nameable {

    fun contains(key: String): Boolean

    fun contains(fieldName: String, fieldValue: Any): Boolean

    fun get(key: String): Optional<Document>

    fun get(fieldName: String, fieldValue: Any): Collection<Document>

    fun get(key: String, def: Document): Document

    fun update(key: String, newValue: Document): Boolean

    fun insert(key: String, value: Document): Boolean

    fun delete(key: String): Boolean

    fun keys(): Collection<String>

    fun updateAsync(key: String, newValue: Document): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync { update(key, newValue) }
    }

    fun containsAsync(key: String): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync { contains(key) }
    }

    fun getAsync(key: String): CompletableFuture<Optional<Document>> {
        return CompletableFuture.supplyAsync { get(key) }
    }

    fun getAsync(key: String, def: Document): CompletableFuture<Document> {
        return CompletableFuture.supplyAsync { get(key, def) }
    }

    fun insertAsync(key: String, value: Document): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync { insert(key, value) }
    }

    fun deleteAsync(key: String): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync { delete(key) }
    }

    fun keysAsync(): CompletableFuture<Collection<String>> {
        return CompletableFuture.supplyAsync { keys() }
    }

}