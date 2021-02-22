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

import eu.vironlab.vextension.database.AbstractDatabase
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.document.DocumentManagement
import java.sql.ResultSet
import java.util.*
import java.util.ArrayList

/**
 * Some Parts are from https://github.com/CloudNetService/CloudNet-v3/blob/master/cloudnet/src/main/java/de/dytanic/cloudnet/database/sql/SQLDatabase.java (Licensed with MIT)
 */
class SqlDatabase(override val name: String, val client: SqlDatabaseClient) : AbstractDatabase(name) {
    val TABLE_COLUMN_KEY = "key"
    val TABLE_COLUMN_VALUE = "value"

    init {
        client.executeUpdate("CREATE TABLE IF NOT EXISTS $name( `$TABLE_COLUMN_KEY` VARCHAR(1024) NOT NULL, `$TABLE_COLUMN_VALUE` JSON NOT NULL );")
    }

    override fun get(key: String): Optional<Document> {
        if (!contains(key)) {
            return Optional.empty()
        }
        return Optional.ofNullable(
            this.client.executeQuery(
                "SELECT * FROM `$name` WHERE `$TABLE_COLUMN_KEY` = ? ",
                object : IThrowableCallback<ResultSet, Document?> {
                    override fun call(t: ResultSet): Document? {
                        return if (t.next()) {
                            DocumentManagement.newJsonDocument(
                                t.getString(TABLE_COLUMN_KEY),
                                t.getString(TABLE_COLUMN_VALUE)
                            )
                        } else {
                            null
                        }
                    }
                },
                key
            )
        )
    }

    override fun get(key: String, value: Any): Collection<Document> {
        return this.client.executeQuery(
            "SELECT `$TABLE_COLUMN_VALUE` FROM `$name` WHERE `$TABLE_COLUMN_VALUE` LIKE ?",
            object : IThrowableCallback<ResultSet, MutableCollection<Document>> {
                override fun call(t: ResultSet): MutableCollection<Document> {
                    val documents: MutableList<Document> = mutableListOf()
                    while (t.next()) {
                        documents.add(
                            DocumentManagement.newJsonDocument(
                                t.getString(TABLE_COLUMN_KEY),
                                t.getString(TABLE_COLUMN_VALUE)
                            )
                        )
                    }
                    return documents
                }
            },
            "%\"$key\":$value%"
        )
    }

    override fun insert(key: String, value: Document): Boolean {
        return this.client.executeUpdate(
            "INSERT INTO `" + this.name + "` ( `" + TABLE_COLUMN_KEY + "` , `" + TABLE_COLUMN_VALUE + "` ) VALUES ( ? , ? );",
            key, value.toJson()
        ) != -1;
    }

    override fun update(key: String, newValue: Document): Boolean {
        return this.client.executeUpdate(
            "UPDATE `" + this.name + "` SET `" + TABLE_COLUMN_VALUE + "` =? WHERE " + TABLE_COLUMN_KEY + "=?",
            newValue.toJson(), key
        ) != -1;
    }

    override fun delete(key: String): Boolean {
        return this.client.executeUpdate(
            "DELETE FROM `" + this.name + "` WHERE " + TABLE_COLUMN_KEY + "=?",
            key
        ) != -1;
    }

    override fun contains(key: String): Boolean {
        return this.client.executeQuery(
            "SELECT `" + TABLE_COLUMN_KEY + "` FROM `" + this.name + "` WHERE `" + TABLE_COLUMN_KEY + "` =?",
            object : IThrowableCallback<ResultSet, Boolean> {
                override fun call(t: ResultSet): Boolean {
                    return t.next()
                }
            },
            key
        );
    }

    override fun contains(key: String, value: Any): Boolean {
        return get(key, value).isNotEmpty()
    }

    override fun keys(): Collection<String> {
        return this.client.executeQuery(
            "SELECT `" + TABLE_COLUMN_KEY + "` FROM `" + this.name + "`;",
            object : IThrowableCallback<ResultSet, MutableCollection<String>> {
                override fun call(t: ResultSet): MutableCollection<String> {
                    val keys: MutableCollection<String> = mutableListOf()
                    while (t.next()) {
                        keys.add(t.getString(TABLE_COLUMN_KEY))
                    }
                    return keys
                }
            })
    }

    override fun clear(): Boolean {
        this.client.executeUpdate("TRUNCATE TABLE " + this.name)
        return true
    }

    override fun forEach(func: (String, Document) -> Unit) {
        keys().forEach {
            func.invoke(it, get(it).get())
        }
    }
}