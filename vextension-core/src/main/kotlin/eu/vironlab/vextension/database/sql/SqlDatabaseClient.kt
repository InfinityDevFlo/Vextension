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

import eu.vironlab.vextension.database.*
import eu.vironlab.vextension.document.DefaultDocument
import java.sql.Driver
import java.sql.DriverManager


class SqlDatabaseClient(val connectionString: String) : DatabaseClient {

    val TABLE_KEY: String = "name"
    val TABLE_DOCUMENT = "document"

    init {

    }

    fun connect() {
        DriverManager.getConnection(connectionString)
    }

    override fun <T : DatabaseObject> getDatabase(name: String, parsedClass: Class<T>): Database<T> {
        if (parsedClass.isAnnotationPresent(NewDatabaseObject::class.java)) {
            executeUpdate("CREATE TABLE IF NOT EXISTS `" + name + "` (" + TABLE_KEY + " VARCHAR(64) PRIMARY KEY, " + TABLE_DOCUMENT + " TEXT);");
            return SqlDatabase(
                name,
                this
            )
        } else {
            throw InvalidDatabaseObjectException("You need to add the ${NewDatabaseObject::class.java.name} Annotation to your Object")
        }
    }

    override fun getBasicDatabase(name: String): Database<DefaultDocument> {
        executeUpdate("CREATE TABLE IF NOT EXISTS `" + name + "` (" + TABLE_KEY + " VARCHAR(64) PRIMARY KEY, " + TABLE_DOCUMENT + " TEXT);");
        return SqlDatabase<DefaultDocument>(
            name,
            this
        )
    }

    override fun exists(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun drop(name: String) {
        TODO("Not yet implemented")
    }

    fun executeUpdate(query: String) {

    }
}