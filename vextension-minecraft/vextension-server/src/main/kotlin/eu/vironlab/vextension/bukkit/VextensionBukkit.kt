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

package eu.vironlab.vextension.bukkit

import eu.vironlab.vextension.Vextension
import eu.vironlab.vextension.database.ClientNotInitializedException
import eu.vironlab.vextension.database.DatabaseClient
import eu.vironlab.vextension.database.DatabaseClientType
import eu.vironlab.vextension.database.DatabaseConnectionData
import eu.vironlab.vextension.database.mongodb.MongoDatabaseClient
import eu.vironlab.vextension.database.sql.SqlDatabaseClient
import eu.vironlab.vextension.scoreboard.Sidebar
import eu.vironlab.vextension.scoreboard.builder.sidebar
import eu.vironlab.vextension.rest.wrapper.vironlab.VironLabAPI
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class VextensionBukkit : JavaPlugin(), Vextension {

    private var databaseClient: DatabaseClient? = null

    companion object {
        @JvmStatic
        lateinit var instance: VextensionBukkit
    }

    override fun onLoad() {
        instance = this
        val sidebar: Sidebar = sidebar {
            this.title = "Ein Toller Titel"
            addLine {
                this.name = "PlayerName"
                this.content = "§b%name%"
                this.score = 15
                 proceed { line, playerId ->
                     line.content.replace("%name%", Bukkit.getPlayer(playerId)!!.name)
                 }
            }
        }
    }

    override fun getDatabaseClient(): DatabaseClient {
        return this.databaseClient ?: throw ClientNotInitializedException("You have to init the client first")
    }

    override fun getVironLabAPI(): VironLabAPI {
        TODO("Not yet implemented")
    }


    override fun initDatabase(type: DatabaseClientType, connectionData: DatabaseConnectionData) {
        when(type) {
             DatabaseClientType.SQL -> {
                 this.databaseClient = SqlDatabaseClient(connectionData.toSql())
             }
            DatabaseClientType.MONGO -> {
                this.databaseClient = MongoDatabaseClient(connectionData.database, connectionData.toMongo())
            }
        }
    }
}