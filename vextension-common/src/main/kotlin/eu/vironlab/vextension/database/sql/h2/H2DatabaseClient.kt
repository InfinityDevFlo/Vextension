/**
 *   Copyright © 2020 | vironlab.eu | Licensed under the GNU General Public license Version 3<p>
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

package eu.vironlab.vextension.database.sql.h2

import eu.vironlab.vextension.concurrent.task.QueuedTask
import eu.vironlab.vextension.concurrent.task.queueTask
import eu.vironlab.vextension.database.Database
import eu.vironlab.vextension.database.connectiondata.ConnectionData
import eu.vironlab.vextension.database.connectiondata.FileConnectionData
import eu.vironlab.vextension.database.sql.AbstractSqlDatabaseClient
import eu.vironlab.vextension.extension.use
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import org.h2.Driver


class H2DatabaseClient constructor(data: ConnectionData) : AbstractSqlDatabaseClient() {

    companion object {
        init {
            Driver.load()
        }
    }

    val connectionData: FileConnectionData
    lateinit var connection: Connection

    init {
        if (data !is FileConnectionData) {
            throw IllegalStateException("Cannot create h2 Database without FileConnectionData")
        }
        this.connectionData = data
    }

    override fun <T> executeQuery(query: String, errorAction: (Throwable) -> Unit, action: (ResultSet) -> T): T {
        var exception: Throwable? = null
        try {
            connection.prepareStatement(query)?.use {
                try {
                    it.executeQuery()?.use { resultSet -> return action.invoke(resultSet) }
                } catch (ex: Exception) {
                    exception = ex
                }
            }
        } catch (ex: SQLException) {
            exception = ex
        }
        if (exception != null) {
            errorAction.invoke(exception!!)
        }
        throw IllegalStateException("There was an error while executing the query")
    }

    override fun executeUpdate(query: String): Int {
        try {
            connection.prepareStatement(query)?.use { return it.executeUpdate() }
        } catch (exception: SQLException) {
            exception.printStackTrace()
        }
        return -1
    }

    override fun init(): Boolean {
        this.connection = DriverManager.getConnection("jdbc:h2:${this.connectionData.file.absolutePath}")
        return true
    }


    override fun getDatabase(name: String): QueuedTask<Database> {
        return queueTask {
            return@queueTask H2Database(name, this)
        }
    }

    override val name: String = "h2"

    override fun close() {
        connection.close()
    }
}