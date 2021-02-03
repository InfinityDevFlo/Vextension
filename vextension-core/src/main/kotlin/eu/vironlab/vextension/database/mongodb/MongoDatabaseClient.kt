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


package eu.vironlab.vextension.database.mongodb

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import eu.vironlab.vextension.database.*
import eu.vironlab.vextension.document.DefaultDocument


class MongoDatabaseClient(val dbName: String, val connectionString: String) : DatabaseClient {

    var mongoClient: MongoClient
    val database: MongoDatabase

    init {
        this.mongoClient = MongoClients.create(connectionString)
        this.database = mongoClient.getDatabase(dbName)
    }

    override fun <T : DatabaseObject> getDatabase(name: String, parsedClass: Class<T>): Database<T> {
        if (parsedClass.isAnnotationPresent(NewDatabaseObject::class.java)) {
            if (!exists(name)) {
                database.createCollection(name)
            }
            return eu.vironlab.vextension.database.mongodb.MongoDatabase<T>(
                name,
                parsedClass,
                database.getCollection(name)
            )
        } else {
            throw InvalidDatabaseObjectException("You need to add the ${NewDatabaseObject::class.java.name} Annotation to your Object")
        }
    }

    override fun getBasicDatabase(name: String): Database<DefaultDocument> {
        if (!exists(name)) {
            database.createCollection(name)
        }
        return eu.vironlab.vextension.database.mongodb.MongoDatabase<DefaultDocument>(
            name,
            DefaultDocument::class.java,
            database.getCollection(name)
        )
    }

    override fun exists(name: String): Boolean {
        return database.listCollectionNames().contains(name)
    }

    override fun drop(name: String) {
        if (exists(name)) {
            database.getCollection(name).drop()
        }
    }
}