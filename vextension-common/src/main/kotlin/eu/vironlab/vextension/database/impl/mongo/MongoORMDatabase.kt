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
import eu.vironlab.vextension.database.AbstractORMDatabase
import eu.vironlab.vextension.database.ORMModel
import eu.vironlab.vextension.document.DocumentManagement
import java.util.*
import org.bson.Document


class MongoORMDatabase<K, V : ORMModel<V>>(override val name: String, val mongoCollection: MongoCollection<Document>, clazz: Class<V>) :
    AbstractORMDatabase<K, V>(clazz) {

    private fun toBson(document: eu.vironlab.vextension.document.Document): Document {
        return Document.parse(document.toJson())
    }

    private fun fromBson(document: Document): eu.vironlab.vextension.document.Document {
        return DocumentManagement.newJsonDocument(document.getString(COLLECTION_KEY), document.toJson())
    }

    override fun contains(key: K): Boolean {
        return this.mongoCollection.find(BasicDBObject(COLLECTION_KEY, key)).cursor().hasNext()
    }
    
    override fun get(key: K, def: V): V {
        TODO("Not yet implemented")
    }

    override fun insert(key: K, value: V): Boolean {
        TODO("Not yet implemented")
    }

    override fun delete(key: K): Boolean {
        TODO("Not yet implemented")
    }

    override fun keys(): Collection<K> {
        TODO("Not yet implemented")
    }

    override fun contains(fieldName: String, fieldValue: Any): Boolean {
        return this.mongoCollection.find(BasicDBObject(fieldName, fieldValue)).cursor().hasNext()
    }

    override fun get(key: K): Optional<V> {
        val cursor: MongoCursor<Document> = this.mongoCollection.find(BasicDBObject(COLLECTION_KEY, key)).cursor()
        if (!cursor.hasNext()) {
            return Optional.empty()
        }
        val rs: V = this.ormConstructor.newInstance()!!
        return Optional.ofNullable(rs.init(fromBson(cursor.next())))
    }

    override fun get(fieldName: String, fieldValue: Any): Collection<V> {
        TODO("Not yet implemented")
    }

}