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

package eu.vironlab.vextension.database.sql

import eu.vironlab.vextension.concurrent.task.QueuedTask
import eu.vironlab.vextension.concurrent.task.queueTask
import eu.vironlab.vextension.database.Database
import eu.vironlab.vextension.database.sql.util.SqlRegistry
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.document.document
import java.sql.ResultSet

abstract class AbstractSqlDatabase(dbname: String, val client: AbstractSqlDatabaseClient) : Database {

    val TABLE_KEY: String
    val creator: TableCreator
    val insertValueNames: String

    init {
        if (!SqlRegistry.creators().containsKey(dbname)) {
            throw IllegalStateException("Cannot Create SqlTable without registered Creator")
        }
        this.creator = SqlRegistry.creators().get(dbname)!!
        this.TABLE_KEY = creator.key.name
        client.executeUpdate(creator.createQuery())
        val valueNames: StringBuilder = StringBuilder(" ")
        creator.entries.forEach {
            valueNames.append("`${it.name}`, ")
        }
        this.insertValueNames = valueNames.toString().substring(0, valueNames.toString().length - 2)
    }


    override fun contains(key: String): QueuedTask<Boolean> {
        return contains(TABLE_KEY, key)
    }

    override fun contains(fieldName: String, fieldValue: Any): QueuedTask<Boolean> {
        return queueTask {
            client.executeQuery(
                "SELECT `$TABLE_KEY` FROM `${name}` WHERE `${fieldName}`='${fieldValue}'",
                Throwable::printStackTrace,
                ResultSet::next
            )

        }
    }

    override fun insert(key: String, value: Document): QueuedTask<Boolean> {
        return queueTask {
            val valueObjs: StringBuilder = StringBuilder()
            creator.entries.forEach {
                valueObjs.append("'${value.getString(it.documentName).toString()}', ")
            }
            val query = "INSERT INTO `${name}` (${insertValueNames}) VALUES (${
                valueObjs.toString().let { it.substring(0, it.length - 2) }
            } ) "
            client.executeUpdate(query) != -1
        }
    }

    override fun update(key: String, newValue: Document): QueuedTask<Boolean> {
        return queueTask {
            val update: StringBuilder = StringBuilder()
            creator.entries.forEach {
                update.append("`${it.name}` = '${newValue.getString(it.documentName).toString()}', ")
            }
            val query = "UPDATE `${name}` SET ${
                update.toString().let { it.substring(0, it.length - 2) }
            } WHERE `$TABLE_KEY` = '${key}'"
            client.executeUpdate(query) != -1
        }
    }

    override fun delete(key: String): QueuedTask<Boolean> {
        return queueTask {
            client.executeUpdate("DELETE FROM `${name}` WHERE $TABLE_KEY=${key}") != -1
        }
    }

    override fun delete(fieldName: String, fieldValue: Any): QueuedTask<Boolean> {
        return queueTask {
            client.executeUpdate("DELETE FROM `${name}` WHERE ${fieldName}=${fieldValue}") != -1
        }
    }

    override fun keys(): QueuedTask<Collection<String>> {
        return queueTask {
            client.executeQuery("SELECT * FROM `${name}`", Throwable::printStackTrace) {
                val rs: MutableCollection<String> = mutableListOf()
                while (it.next()) {
                    rs.add(it.getString(creator.key.name))
                }
                rs
            }
        }
    }

    override fun get(key: String, def: Document): QueuedTask<Document> {
        return queueTask {
            return@queueTask get(key).complete() ?: run {
                insert(key, def).complete()
                def
            }
        }
    }

    override fun get(key: String): QueuedTask<Document?> {
        return queueTask {
            if (!contains(key).complete()) {
                return@queueTask null
            }
            return@queueTask client.executeQuery(
                "SELECT * FROM `${name}` WHERE `$TABLE_KEY`='${key}'",
                Throwable::printStackTrace
            ) {
                if (it.next()) {
                    val rs: Document = document()
                    for (entry in creator.entries) {
                        if (it.next()) {
                            rs.append(entry.documentName, it.getObject(entry.name))
                        }
                    }
                    return@executeQuery rs
                } else {
                    null
                }
            }
        }
    }

    override fun get(fieldName: String, fieldValue: Any): QueuedTask<Collection<Document>> {
        return queueTask {
            if (!contains(fieldName, fieldValue).complete()) {
                return@queueTask mutableListOf()
            }
            return@queueTask client.executeQuery(
                "SELECT * FROM `${name}` WHERE ${fieldName}=${fieldValue}", Throwable::printStackTrace
            ) {
                val rs = mutableListOf<Document>()
                while (it.next()) {
                    val temprs: Document = document()
                    for (entry in creator.entries) {
                        if (it.next()) {
                            temprs.append(entry.documentName, it.getObject(entry.name))
                        }
                    }
                    rs.add(temprs)
                }
                return@executeQuery rs
            }
        }
    }

}