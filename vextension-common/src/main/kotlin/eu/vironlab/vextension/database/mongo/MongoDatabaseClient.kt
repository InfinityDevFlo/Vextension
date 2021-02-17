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

package eu.vironlab.vextension.database.mongo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import eu.vironlab.vextension.database.*
import eu.vironlab.vextension.dependency.DependencyLoader
import eu.vironlab.vextension.document.DefaultDocument
import eu.vironlab.vextension.document.Document

class MongoDatabaseClient(val connectionData: RemoteConnectionData) : AbstractDatabaseClient() {

    lateinit var mongoClient: MongoClient
    lateinit var database: com.mongodb.client.MongoDatabase

    override fun init() {
        DependencyLoader.require("org.mongodb:mongodb-driver-sync:4.2.0")
        DependencyLoader.require("org.mongodb:mongodb-driver-core:4.2.0")
        DependencyLoader.require("org.mongodb:bson:4.2.0")
        this.mongoClient = MongoClients.create(connectionData.toMongo())
        this.database = this.mongoClient.getDatabase(connectionData.database)
    }

    override fun <T, K> getDatabase(name: String, parsedClass: Class<T>): Database<T, K> {
        DatabaseUtil.getInfo(parsedClass)
            .orElseThrow { IllegalStateException("Cannot load Database from Unknown Object") }
        return MongoDatabase<T, K>(name, parsedClass, database)
    }

    override fun getBasicDatabase(name: String): Database<DefaultDocument,  String> {
        return getDatabase(name, DefaultDocument::class.java)
    }

    override fun exists(name: String): Boolean {
        return this.database.listCollectionNames().contains(name)
    }

    override fun drop(name: String): Boolean {
        return if (!exists(name)) {
            false
        } else {
            this.database.getCollection(name).drop()
            return true
        }
    }

}