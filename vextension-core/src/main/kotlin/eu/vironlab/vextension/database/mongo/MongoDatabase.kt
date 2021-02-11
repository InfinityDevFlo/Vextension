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
import eu.vironlab.vextension.database.AbstractDatabase
import org.bson.Document
import com.mongodb.client.FindIterable


open class MongoDatabase<T, K>(override val name: String, clazz: Class<T>, database: MongoDatabase) :
    AbstractDatabase<T, K>(clazz) {

    val collection: MongoCollection<Document>

    init {
        if (!database.listCollectionNames().contains(name)) {
            database.createCollection(name)
        }
        this.collection = database.getCollection(name)
    }

    override fun get(key: String, value: Any): Collection<T> {
        if (!contains(key, value!!)) {
            return mutableListOf()
        }
        val queryObject: BasicDBObject = BasicDBObject(this.classInfo.key, key)
        val cursor = this.collection.find(queryObject).cursor()
        val rs = mutableListOf<T>()
        while (cursor.hasNext()) {
            val bson = cursor.next()
            val instance: T? = this.parsedClass.getConstructor().newInstance()
            val info = this.classInfo
            if (instance != null) {
                instance!!::class.java.declaredFields.forEach {
                    if (!info.ignoredFields.contains(it.name)) {
                        val name = if (info.specificNames.containsKey(it.name)) {
                            info.specificNames.get(it.name)!!
                        } else {
                            it.name
                        }
                        it.set(instance, bson.get(name)!!)
                    }
                }
                rs.add(instance)
            }
        }
        return rs
    }

    override fun insert(key: K, value: T): Boolean {
        if (contains(key)) {
            throw IllegalStateException("Cannot insert the same key twice - ${key}")
        }
        val queryObject = BasicDBObject(this.classInfo.key, key)
        val bson: Document = Document(this.classInfo.key, key).parse(parsedClass, value)
        this.collection.insertOne(bson)
        return true
    }

    override fun delete(key: K): Boolean {
        return if (contains(key)) {
            this.collection.deleteOne(BasicDBObject(this.classInfo.key, key!!))
            true
        } else {
            false
        }
    }

    override fun forEach(func: (K, T) -> Unit) {
        this.collection.find(BasicDBObject()).cursor().forEachRemaining {
            func.invoke(
                it.get(
                    this.classInfo.key,
                    this.parsedClass.getDeclaredField(this.classInfo.keyField).type as Class<K>
                ),
                it.toInstance(parsedClass, this.classInfo)
            )
        }
    }

    override fun contains(key: String, value: Any): Boolean {
        return this.collection.find(BasicDBObject(key, value)).cursor().hasNext()
    }

    override fun keys(): Collection<K> {
        val rs: MutableCollection<K> = mutableListOf()
        val findIterable: FindIterable<*> = this.collection.find()
        findIterable.cursor().forEachRemaining { i: Any ->
            rs.add(
                (i as Document).get(
                    this.classInfo.key,
                    this.parsedClass.getDeclaredField(this.classInfo.keyField).type as Class<K>
                )
            )
        }
        return rs
    }

    override fun clear(): Boolean {
        this.collection.deleteMany(BasicDBObject())
        return true
    }

}