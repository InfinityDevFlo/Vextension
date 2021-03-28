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

import com.mongodb.BasicDBObject
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoCursor
import eu.vironlab.vextension.database.Database
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.document.DocumentManagement
import java.util.*
import org.bson.Document as BsonDocument

class MongoDatabase(override val name: String, val mongoCollection: MongoCollection<BsonDocument>) : Database<String, Document> {

    val COLLECTION_KEY: String = "__key__"

    override fun contains(key: String): Boolean {
        return this.mongoCollection.find(BasicDBObject(COLLECTION_KEY, key)).cursor().hasNext()
    }

    private fun toBson(document: Document): BsonDocument {
        return BsonDocument.parse(document.toJson())
    }

    private fun fromBson(document: BsonDocument): Document {
        return DocumentManagement.newJsonDocument(document.getString(COLLECTION_KEY), document.toJson())
    }

    override fun get(key: String): Optional<Document> {
        val cursor: MongoCursor<BsonDocument> = this.mongoCollection.find(BasicDBObject(COLLECTION_KEY, key)).cursor()
        if (!cursor.hasNext()) {
            return Optional.empty()
        }
        return Optional.of(fromBson(cursor.next()))
    }

    override fun get(key: String, def: Document): Document {
        val cursor: MongoCursor<BsonDocument> = this.mongoCollection.find(BasicDBObject(COLLECTION_KEY, key)).cursor()
        return if (cursor.hasNext()) {
            fromBson(cursor.next())
        } else {
            this.mongoCollection.insertOne(toBson(def.insert(COLLECTION_KEY, key)))
            def
        }
    }

    override fun insert(key: String, value: Document): Boolean {
        if (contains(key)) {
            return false
        }
        this.mongoCollection.insertOne(toBson(value.insert(COLLECTION_KEY, key)))
        return true
    }

    override fun delete(key: String): Boolean {
        if (!contains(key)) {
            return false
        }
        this.mongoCollection.deleteMany(BasicDBObject(COLLECTION_KEY, key))
        return true
    }

    override fun keys(): Collection<String> {
        val rs = mutableListOf<String>()
        this.mongoCollection.find(BasicDBObject()).cursor().forEach {
            rs.add(it.getString(COLLECTION_KEY))
        }
        return rs
    }

    override fun contains(fieldName: String, fieldValue: Any): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(fieldName: String, fieldValue: Any): Collection<Document> {
        TODO("Not yet implemented")
    }
}