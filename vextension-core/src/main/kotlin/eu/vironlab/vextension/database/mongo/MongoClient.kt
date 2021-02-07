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
import eu.vironlab.vextension.document.DefaultDocument
import eu.vironlab.vextension.document.Document

class MongoClient(val connectionString: String, val targetDatabase: String) : AbstractDatabaseClient() {

    var mongoClient: MongoClient? = null

    override fun init() {
        this.mongoClient = MongoClients.create(connectionString)
    }

    override fun <T> getDatabase(name: String, parsedClass: Class<T>): Database<T> {
        this.mongoClient ?: throw ClientNotInitializedException("You have to Initialize the Client first")
        return MongoDatabase(name, this, parsedClass)
    }

    override fun getBasicDatabase(name: String): Database<BasicDatabaseObject> {
        return getDatabase(name, BasicDatabaseObject::class.java)
    }

    override fun exists(name: String): Boolean {
        this.mongoClient ?: throw ClientNotInitializedException("You have to Initialize the Client first")
        return this.mongoClient!!.getDatabase(targetDatabase).listCollectionNames().contains(name)
    }

    override fun drop(name: String) {
        this.mongoClient ?: throw ClientNotInitializedException("You have to Initialize the Client first")
        this.mongoClient!!.getDatabase(targetDatabase).getCollection(name).drop()
    }
}