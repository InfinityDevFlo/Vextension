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

import com.mongodb.BasicDBObject
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import eu.vironlab.vextension.concurrent.scheduleAsync
import eu.vironlab.vextension.database.AbstractDatabase
import eu.vironlab.vextension.document.Document
import java.util.*


open class MongoDatabase(override val name: String, database: MongoDatabase) :
    AbstractDatabase(name) {

    val collection: MongoCollection<org.bson.Document>
    val COLLECTION_KEY = "__key__"

    init {
        if (!database.listCollectionNames().contains(name)) {
            database.createCollection(name)
        }
        this.collection = database.getCollection(name)
    }

    override fun get(key: String): Optional<Document> {
        if (!contains(key)) {
            return Optional.empty()
        }else {
            val cursor = this.collection.find(BasicDBObject(COLLECTION_KEY, key)).cursor()
            if (!cursor.hasNext()) {
                return Optional.empty()
            }
            val doc = cursor.next()
            return Optional.of(doc.toDocument(doc.getString(COLLECTION_KEY)))
        }
    }

    override fun get(key: String, value: Any): Collection<Document> {
        val rs = mutableListOf<Document>()
        val cursor = this.collection.find(BasicDBObject(key, value)).cursor()
        while (cursor.hasNext()) {
            val doc = cursor.next()
            rs.add(doc.toDocument(doc.getString(COLLECTION_KEY)))
        }
        return rs
    }

    override fun insert(key: String, value: Document): Boolean {
        if (contains(key)) {
            return false
        }
        this.collection.insertOne(value.insert(COLLECTION_KEY, key).toBson())
        return true
    }

    override fun update(key: String, newValue: Document): Boolean {
        this.collection.updateOne(BasicDBObject(COLLECTION_KEY, key), newValue.toBson())
        return true
    }

    override fun delete(key: String): Boolean {
        if (!contains(key)) {
            return false
        }
        this.collection.deleteOne(BasicDBObject(COLLECTION_KEY, key))
        return true
    }

    override fun contains(key: String): Boolean {
        return this.collection.find(BasicDBObject(COLLECTION_KEY, key)).cursor().hasNext()
    }

    override fun contains(key: String, value: Any): Boolean {
        return this.collection.find(BasicDBObject(key, value)).cursor().hasNext()
    }

    override fun keys(): Collection<String> {
        val rs: MutableCollection<String> = mutableListOf()
        val cursor = this.collection.find(BasicDBObject()).cursor()
        while (cursor.hasNext()) {
            rs.add(cursor.next().getString(COLLECTION_KEY))
        }
        return rs
    }

    override fun clear(): Boolean {
        this.collection.deleteMany(BasicDBObject())
        return true
    }

    override fun forEach(func: (String, Document) -> Unit) {
        scheduleAsync {
            keys().forEach {
                func.invoke(it, get(it).get())
            }
        }
    }


}