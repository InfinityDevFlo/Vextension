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

import com.mongodb.BasicDBObject
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import eu.vironlab.vextension.concurrent.AsyncTask
import eu.vironlab.vextension.concurrent.AsyncUtil
import eu.vironlab.vextension.database.Database
import eu.vironlab.vextension.database.DatabaseObject
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.document.DocumentManagement
import java.util.*
import java.util.function.BiConsumer


class MongoDatabase<T : DatabaseObject>(
    override val name: String,
    val parsedClass: Class<T>,
    val mongoDatabase: MongoCollection<org.bson.Document>
) :
    Database<T> {

    val COLLECTION_KEY: String = "__key__"

    init {
        mongoDatabase
    }

    private fun parse(bson: org.bson.Document): T {
        bson.remove(COLLECTION_KEY)
        val document: Document = DocumentManagement.newJsonDocument(bson.toJson())
        val constructor = parsedClass.getConstructor()
        val parsed = constructor.newInstance()
        parsed.init(document)
        return parsed
    }

    override fun get(key: String): Optional<T> {
        if (!contains(key)) {
            return Optional.ofNullable(null)
        }
        val findIterable: FindIterable<org.bson.Document> = mongoDatabase.find(BasicDBObject(COLLECTION_KEY, key))
        if (findIterable.iterator().hasNext()) {
            val bsonDocument: org.bson.Document = findIterable.iterator().next()
            return Optional.of(parse(bsonDocument))
        } else {
            return Optional.ofNullable(null)
        }
    }

    override fun contains(key: String): Boolean {
        return mongoDatabase.find(BasicDBObject(COLLECTION_KEY, key)).iterator().hasNext()
    }

    override fun insert(key: String, obj: T): Boolean {
        if (!contains(key)) {
            val bsonDocument: org.bson.Document = org.bson.Document.parse(obj.toDocument().toJson())
            bsonDocument.append(COLLECTION_KEY, key)
            mongoDatabase.insertOne(bsonDocument)
            return true
        }
        return false
    }

    override fun get(field: String, value: String): Collection<T> {
        val result: MutableList<T> = mutableListOf()
        mongoDatabase.find(BasicDBObject(field, value)).iterator().forEach {
            result.add(parse(it))
        }
        return result
    }

    override fun getAllObjects(): MutableMap<String, T> {
        val result: MutableMap<String, T> = mutableMapOf()
        mongoDatabase.find(BasicDBObject()).iterator().forEach {
            result.put(it.getString(COLLECTION_KEY), parse(it))
        }
        return result
    }

    override fun keys(): Collection<String> {
        val rs: MutableCollection<String> = ArrayList()
        val findIterable: FindIterable<*> = mongoDatabase.find()
        findIterable.cursor().forEachRemaining {
            rs.add((it as org.bson.Document).getString(COLLECTION_KEY))
        }
        return rs
    }

    override fun update(key: String, newObj: T): Boolean {
        if (!contains(key)) {
            return false
        }
        mongoDatabase.replaceOne(
            BasicDBObject(COLLECTION_KEY, key),
            org.bson.Document.parse(newObj.toDocument().toJson())
        )
        return true
    }

    override fun delete(key: String): Boolean {
        if (!contains(key)) {
            return false
        }
        mongoDatabase.deleteOne(BasicDBObject(COLLECTION_KEY, key))
        return true
    }

    override fun clear(): Boolean {
        mongoDatabase.deleteMany(BasicDBObject())
        return true
    }

    override fun getDocumentsCount(): Long {
        return mongoDatabase.countDocuments()
    }

    override fun forEach(consumer: BiConsumer<String, T>): Boolean {
        getAllObjects().forEach {
            consumer.accept(it.key, it.value)
        }
        return true
    }

    override fun insertAsync(key: String, obj: T): AsyncTask<Boolean> {
        return AsyncUtil.schedule { insert(key, obj) }
    }

    override fun getAsync(key: String): AsyncTask<Optional<T>> {
        return AsyncUtil.schedule { get(key) }
    }

    override fun getAsync(field: String, value: String): AsyncTask<Collection<T>> {
        return AsyncUtil.schedule { get(field, value) }
    }

    override fun getAllObjectsAsync(): AsyncTask<MutableMap<String, T>> {
        return AsyncUtil.schedule { getAllObjects() }
    }

    override fun keysAsync(): AsyncTask<Collection<String>> {
        return AsyncUtil.schedule { keys() }
    }

    override fun updateAsync(key: String, newObj: T): AsyncTask<Boolean> {
        return AsyncUtil.schedule { update(key, newObj) }
    }

    override fun deleteAsync(key: String): AsyncTask<Boolean> {
        return AsyncUtil.schedule { delete(key) }
    }

    override fun containsAsync(key: String): AsyncTask<Boolean> {
        return AsyncUtil.schedule { contains(key) }
    }

    override fun clearAsync(): AsyncTask<Boolean> {
        return AsyncUtil.schedule { clear() }
    }

    override fun getDocumentsCountAsync(): AsyncTask<Long> {
        return AsyncUtil.schedule { getDocumentsCount() }
    }

    override fun forEachAsync(consumer: BiConsumer<String, T>): AsyncTask<Boolean> {
        return AsyncUtil.schedule { forEach(consumer) }
    }
}