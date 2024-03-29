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

package eu.vironlab.vextension.database.mongo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import eu.vironlab.vextension.concurrent.task.QueuedTask
import eu.vironlab.vextension.concurrent.task.queueTask
import eu.vironlab.vextension.database.Database
import eu.vironlab.vextension.database.DatabaseClient
import eu.vironlab.vextension.database.connectiondata.ConnectionData
import eu.vironlab.vextension.database.connectiondata.RemoteConnectionData
import com.mongodb.client.MongoDatabase as MongoDB

open class MongoDatabaseClient constructor(connectionData: ConnectionData) : DatabaseClient() {

    companion object {
        @JvmStatic
        var mongoAuthMechanism: String = "SCRAM-SHA-1"
    }

    val remoteConnectionData: RemoteConnectionData
    lateinit var mongoClient: MongoClient
    lateinit var mongoDatabase: MongoDB

    init {
        if (connectionData !is RemoteConnectionData) {
            throw IllegalStateException("Cannot load MongoDB Client without RemoteConnectionData")
        }
        this.remoteConnectionData = connectionData
    }

    override fun init(): Boolean {
        this.mongoClient =
            MongoClients.create("mongodb://${remoteConnectionData.user}:${remoteConnectionData.password}@${remoteConnectionData.host}:${remoteConnectionData.port}/${remoteConnectionData.database}?authMechanism=${mongoAuthMechanism}")
        this.mongoDatabase = this.mongoClient.getDatabase(this.remoteConnectionData.database)
        return true
    }

    override fun close() {
        mongoClient.close()
    }

    override fun dropDatabase(name: String): QueuedTask<Boolean> {
        return queueTask {
            if (!containsDatabase(name).complete()) {
                false
            }
            this.mongoDatabase.getCollection(name).drop()
            true
        }
    }


    override fun containsDatabase(name: String): QueuedTask<Boolean> {
        return queueTask { this.mongoDatabase.listCollectionNames().contains(name) }
    }

    override fun getDatabase(name: String): QueuedTask<Database> {
        return queueTask {
            return@queueTask MongoDatabase(name, this)
        }
    }

    override val name: String = "mongodb"

}
