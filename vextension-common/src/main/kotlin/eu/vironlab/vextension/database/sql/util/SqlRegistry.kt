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

package eu.vironlab.vextension.database.sql.util

import eu.vironlab.vextension.database.sql.ColumnType
import eu.vironlab.vextension.database.sql.TableCreator
import eu.vironlab.vextension.database.sql.TableEntry
import java.lang.reflect.Field

object SqlRegistry {

    @JvmStatic
    private val creators: MutableMap<String, TableCreator> = mutableMapOf()

    @JvmStatic
    fun creators(): Map<String, TableCreator> = creators

    @JvmStatic
    fun addCreator(name: String, creator: TableCreator) {
        creators.put(name, creator)
    }

    @JvmStatic
    fun fromClass(clazz: Class<*>): TableCreator {
        val name = clazz.getAnnotation(SqlName::class.java)
            ?: throw IllegalStateException("The ${SqlName::class.java.name} Annotation is Required")
        val fields = clazz.declaredFields
        val key = fields.filter { it.isAnnotationPresent(SqlKey::class.java) }.firstOrNull()
            ?: throw IllegalStateException("The ${SqlKey::class.java.name} Annotation is Required as Table Key")
        val keyAnnotation = key.getAnnotation(SqlKey::class.java)
        val creator = TableCreator(
            name.name,
            TableEntry(
                key.name,
                getColumnType(key),
                if (keyAnnotation.documentName.equals("field")) {
                    key.name
                } else {
                    keyAnnotation.documentName
                },
                keyAnnotation.length,
                true
            )
        )
        fields.filter { !it.isAnnotationPresent(SqlKey::class.java) }
            .filter { it.isAnnotationPresent(SqlEntry::class.java) }.forEach {
                val data = it.getAnnotation(SqlEntry::class.java)
                creator.addEntry(
                    TableEntry(
                        it.name,
                        data.type,
                        if (data.documentName.equals("field")) {
                            key.name
                        } else {
                            data.documentName
                        },
                        data.length,
                        data.notNull
                    )
                )
            }
        return creator
    }

    private fun getColumnType(field: Field): ColumnType {
        var rs = ColumnType.LONGTEXT
        when (field.type) {
            Int::class.java -> {
                rs = ColumnType.INT
            }
            Long::class.java -> {
                rs = ColumnType.BIGINT
            }
        }
        return rs
    }

}