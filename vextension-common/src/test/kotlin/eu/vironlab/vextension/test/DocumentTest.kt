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
 *   Creation: Freitag 18 Juni 2021 21:07:53<p>
 *<p>
 *   Contact:<p>
 *<p>
 *     Discordserver:   https://discord.gg/wvcX92VyEH<p>
 *     Website:         https://vironlab.eu/ <p>
 *     Mail:            contact@vironlab.eu<p>
 *<p>
 */

package eu.vironlab.vextension.test

import eu.vironlab.vextension.document.document
import eu.vironlab.vextension.extension.getResourceContent
import eu.vironlab.vextension.extension.random
import java.math.BigInteger
import java.util.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import eu.vironlab.vextension.document.DocumentFactory

class DocumentTest {
    init {
        DocumentFactory.instance = DocumentFactory()
    }
    @Test
    fun testDocumentAppendAndGet() {
        val values = mutableMapOf<String, Any>(
            Pair("string", String.random(64)),
            Pair("int", 1234567),
            Pair("long", System.currentTimeMillis()),
            Pair("bigint", BigInteger.ONE),
            Pair("uuid", UUID.randomUUID()),
            Pair("boolean", true)
        )
        val doc = document()
        doc.append(values)
        for ((key, value) in values) {
            assertEquals(doc.get(key), value)
        }
    }

    @Test
    fun testJsonDocument() {
        val doc = DocumentFactory.instance.documentJsonStorage.read(this::class.java.getResourceAsStream("/test.json"))
        assertEquals(doc.jsonStorage().serializeToString(), this::class.java.getResourceContent("/test.json"))
        assertEquals(doc.jsonStorage().serializeToString(), document(DocumentTest()).xmlStorage().serializeToString())
    }

    @Test
    fun testXmlDocument() {
        val doc = DocumentFactory.instance.documentXmlStorage.read(this::class.java.getResourceAsStream("/test.xml"))
        assertEquals(doc.xmlStorage().serializeToString(), this::class.java.getResourceContent("/test.xml"))
        assertEquals(doc.xmlStorage().serializeToString(), document(DocumentTest()).xmlStorage().serializeToString())
    }


    @Test
    fun testYamlDocument() {
        val doc = DocumentFactory.instance.documentYamlStorage.read(this::class.java.getResourceAsStream("/test.yml"))
        assertEquals(doc.yamlStorage().serializeToString(), this::class.java.getResourceContent("/test.yml"))
        assertEquals(doc.yamlStorage().serializeToString(), document(DocumentTest()).xmlStorage().serializeToString())
    }


    inner class DocumentTest(
        val string: String = "Powerfull String",
        val int: Int = 1,
        val id: UUID = UUID.fromString("db8a3d09-da1c-4a3b-a907-3363339a4f19")
    )
}
