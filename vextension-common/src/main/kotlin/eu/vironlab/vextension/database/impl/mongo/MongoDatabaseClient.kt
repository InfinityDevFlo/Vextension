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

package eu.vironlab.vextension.database.impl.mongo

import com.google.inject.Inject
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import eu.vironlab.vextension.annotation.LinkDataStore
import com.mongodb.client.MongoDatabase as MongoDB
import eu.vironlab.vextension.database.Database
import eu.vironlab.vextension.database.DatabaseClient
import eu.vironlab.vextension.database.connectiondata.ConnectionData
import eu.vironlab.vextension.database.connectiondata.RemoteConnectionData
import eu.vironlab.vextension.document.Document

@LinkDataStore<MongoDataStoreClient>(MongoDataStoreClient::class)
open class MongoDatabaseClient @Inject constructor(connectionData: ConnectionData) : DatabaseClient {

    val remoteConnectionData: RemoteConnectionData
    lateinit var mongoClient: MongoClient
    lateinit var mongoDatabase: MongoDB

    init {
        if (connectionData !is RemoteConnectionData) {
            throw IllegalStateException("Cannot load MongoDB Client without RemoteConnectionData")
        }
        this.remoteConnectionData = connectionData as RemoteConnectionData
    }

    override fun init() {
        this.mongoClient = MongoClients.create("mongodb://${remoteConnectionData.user}:${remoteConnectionData.password}@${remoteConnectionData.host}:${remoteConnectionData.port}/${remoteConnectionData.database}")
        this.mongoDatabase = this.mongoClient.getDatabase(this.remoteConnectionData.database)
    }

    override fun close() {
        mongoClient.close()
    }

    override fun dropDatabase(name: String): Boolean {
        if (!containsDatabase(name)) {
            return false
        }
        this.mongoDatabase.getCollection(name).drop()
        return true
    }

    override fun containsDatabase(name: String): Boolean {
        return this.mongoDatabase.listCollectionNames().contains(name)
    }

    override fun getDatabase(name: String): Database<String, Document> {
        if (!containsDatabase(name)) {
            this.mongoDatabase.createCollection(name)
        }
        return MongoDatabase(name, this.mongoDatabase.getCollection(name))
    }

}