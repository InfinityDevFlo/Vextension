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

package eu.vironlab.vextension.database.factory

import com.google.inject.Guice
import eu.vironlab.vextension.annotation.LinkDataStore
import eu.vironlab.vextension.database.DatabaseClient
import eu.vironlab.vextension.database.connectiondata.ConnectionData
import eu.vironlab.vextension.database.data.DataStore
import eu.vironlab.vextension.database.data.DataStoreClient
import eu.vironlab.vextension.database.inject.DataStoreClientInjectorModule
import eu.vironlab.vextension.database.inject.DatabaseClientInjectorModule
import eu.vironlab.vextension.factory.Factory


class DatabaseClientFactory<T : DatabaseClient>(val implClass: Class<T>) : Factory<T> {

    lateinit var connectionData: ConnectionData

    fun connectionData(connectionData: ConnectionData): DatabaseClientFactory<T> {
        this.connectionData = connectionData
        return this
    }

    fun createWithDataStore(): DataStoreClient {
        if (!implClass.isAnnotationPresent(LinkDataStore::class.java)) {
            throw IllegalStateException("No DataStore Implementation Present")
        }
        val storeClass = implClass.getAnnotation(LinkDataStore::class.java).store.java
        val injector =  Guice.createInjector(DataStoreClientInjectorModule<T>(this.connectionData, implClass, create()))
        return injector.getInstance(storeClass)
    }

    override fun create(): T {
        return Guice.createInjector(DatabaseClientInjectorModule(this.connectionData)).getInstance(implClass)
    }

}

fun <T : DatabaseClient> createDatabaseClient(implClass: Class<T>, init: DatabaseClientFactory<T>.() -> Unit): T {
    val clientFactory: DatabaseClientFactory<T> = DatabaseClientFactory(implClass)
    clientFactory.init()
    return clientFactory.create()
}

fun <T: DatabaseClient> createDataStoreClient(implClass: Class<T>, init: DatabaseClientFactory<T>.() -> Unit): DataStoreClient {
    val clientFactory: DatabaseClientFactory<T> = DatabaseClientFactory(implClass)
    clientFactory.init()
    return clientFactory.createWithDataStore()
}



