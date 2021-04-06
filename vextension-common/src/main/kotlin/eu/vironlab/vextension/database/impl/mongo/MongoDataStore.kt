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
import com.google.gson.reflect.TypeToken
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoCollection
import eu.vironlab.vextension.concurrent.Callback
import eu.vironlab.vextension.database.data.DataStore
import eu.vironlab.vextension.database.data.MappingObject
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.document.DocumentManagement
import java.lang.reflect.Type
import java.util.*
import org.bson.Document as BsonDocument


class MongoDataStore<K, V : MappingObject>(
    override val name: String,
    override val storeClass: Class<V>,
    override val initializer: Callback<Document, V>,
    val mongoCollection: MongoCollection<BsonDocument>
) : DataStore<K, V> {

    val COLLECTION_KEY = "__key__"

    private fun toBson(document: Document): BsonDocument {
        return BsonDocument.parse(document.toJson())
    }

    private fun fromBson(document: BsonDocument): Document {
        return DocumentManagement.newJsonDocument(document.getString(COLLECTION_KEY), document.toJson())
    }

    override fun contains(key: K): Boolean {
        return this.mongoCollection.find(BasicDBObject(COLLECTION_KEY, key)).cursor().hasNext()
    }

    override fun contains(fieldName: String, fieldValue: Any): Boolean {
        return this.mongoCollection.find(BasicDBObject(fieldName, fieldValue)).cursor().hasNext()
    }

    override fun get(key: K): Optional<V> {
        val cursor = this.mongoCollection.find(BasicDBObject(COLLECTION_KEY, key)).cursor()
        if (!cursor.hasNext()) {
            return Optional.empty()
        }
        return Optional.of(this.initializer.call(fromBson(cursor.next())))
    }

    override fun get(fieldName: String, fieldValue: Any): Collection<V> {
        val cursor = this.mongoCollection.find(BasicDBObject(fieldName, fieldValue)).cursor()
        return if (cursor.hasNext()) {
            val rs = mutableListOf<V>(this.initializer.call(fromBson(cursor.next())))
            while (cursor.hasNext()) {
                rs.add(this.initializer.call(fromBson(cursor.next())))
            }
            rs
        } else {
            listOf<V>()
        }
    }

    override fun get(key: K, def: V): V {
        return if (!contains(key)) {
            insert(key, def)
            def
        }else {
            get(key).get()
        }
    }

    override fun insert(key: K, value: V): Boolean {
        if (contains(key)) {
            return false
        }
        this.mongoCollection.insertOne(toBson(value.export().insert(COLLECTION_KEY, key!!)))
        return true
    }

    override fun delete(key: K): Boolean {
        if (contains(key)) {
            this.mongoCollection.deleteOne(BasicDBObject(COLLECTION_KEY, key))
            return true
        }
        return false
    }

    override fun keys(): Collection<K> {
        val rs = mutableListOf<K>()
        this.mongoCollection.find(BasicDBObject()).cursor().forEach {
            rs.add(fromBson(it).get<K>(COLLECTION_KEY, object : TypeToken<K>() {}.type).get())
        }
        return rs
    }
}

