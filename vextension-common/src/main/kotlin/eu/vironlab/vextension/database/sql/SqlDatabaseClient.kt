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

import eu.vironlab.vextension.database.AbstractDatabaseClient
import eu.vironlab.vextension.database.Database
import eu.vironlab.vextension.database.RemoteConnectionData
import eu.vironlab.vextension.dependency.DependencyLoader
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

import java.sql.SQLException

/**
 * Some Parts are from https://github.com/CloudNetService/CloudNet-v3/blob/master/cloudnet/src/main/java/de/dytanic/cloudnet/database/sql/SQLDatabase.java (Licensed with MIT)
 */
class SqlDatabaseClient(val connectionData: RemoteConnectionData) : AbstractDatabaseClient() {

    lateinit var connection: Connection

    override fun init() {
        DependencyLoader.require("mysql:mysql-connector-java:8.0.23")
        Class.forName("com.mysql.cj.jdbc.Driver")
        this.connection = DriverManager.getConnection(
            this.connectionData.toSql(),
            this.connectionData.user,
            this.connectionData.password
        )
    }

    override fun getDatabase(name: String): Database {
        return SqlDatabase(name, this)
    }

    override fun exists(name: String): Boolean {
        return getDatabaseNames().contains(name)
    }

    override fun drop(name: String): Boolean {
        if (!exists(name)) {
            return false
        }
        executeUpdate("DROP TABLE $name")
        return true
    }

    override fun close() {
        this.connection.close()
    }

    /**
     * From https://github.com/CloudNetService/CloudNet-v3/
     */
    fun getDatabaseNames(): Collection<String> {
        return executeQuery(
            "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES  where TABLE_SCHEMA='PUBLIC'",
            object : IThrowableCallback<ResultSet, MutableCollection<String>> {
                override fun call(resultSet: ResultSet): MutableCollection<String> {
                    val collection: MutableCollection<String> = mutableListOf()
                    while (resultSet.next()) {
                        collection.add(resultSet.getString("table_name"))
                    }
                    return collection
                }
            }
        )
    }

    /**
     * From https://github.com/CloudNetService/CloudNet-v3/
     */
    fun executeUpdate(query: String, vararg objects: Any): Int {
        try {
            val stmt = this.connection.prepareStatement(query)
            var i = 1
            for (`object` in objects) {
                stmt.setString(i++, `object`.toString())
            }
            return stmt.executeUpdate()
        } catch (exception: SQLException) {
            exception.printStackTrace()
        }
        return -1
    }

    /**
     * From https://github.com/CloudNetService/CloudNet-v3/
     */
    fun <T> executeQuery(query: String, callback: IThrowableCallback<ResultSet, T>, vararg objects: Any): T {
        try {
            val stmt = this.connection.prepareStatement(query)
            var i = 1
            for (`object` in objects) {
                stmt.setString(i++, `object`.toString())
            }
            val rs = stmt.executeQuery()
            return callback.call(rs)
        } catch (e: Throwable) {
            throw e
        }
    }


}