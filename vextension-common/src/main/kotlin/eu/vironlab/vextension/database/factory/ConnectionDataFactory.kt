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

import eu.vironlab.vextension.database.connectiondata.AuthedFileConnectionData
import eu.vironlab.vextension.database.connectiondata.ConnectionData
import eu.vironlab.vextension.database.connectiondata.FileConnectionData
import eu.vironlab.vextension.database.connectiondata.RemoteConnectionData
import eu.vironlab.vextension.factory.Factory
import java.io.File


class ConnectionDataFactory : Factory<ConnectionData> {
    var file: File? = null
    var host: String? = null
    var port: Int? = null
    var database: String? = null
    var user: String? = null
    var password: String? = null
    override fun create(): ConnectionData {
        return if (file != null) {
            if (user != null && password != null) {
                AuthedFileConnectionDataImpl(file!!, user!!, password!!)
            }
            FileConnectionDataImpl(file!!)
        } else {
            if (host == null || port == null || database == null || user == null || password == null) {
                throw IllegalStateException("Cannot create RemoteConnectionData with null Values")
            }
            RemoteConnectionDataImpl(host!!, port!!, database!!, user!!, password!!)
        }
    }

    internal inner class FileConnectionDataImpl(override val file: File) : FileConnectionData

    internal inner class RemoteConnectionDataImpl(
        override val host: String,
        override val port: Int,
        override val database: String,
        override val user: String,
        override val password: String
    ) : RemoteConnectionData

    internal inner class AuthedFileConnectionDataImpl(
        override val file: File, override val user: String,
        override val password: String
    ) : AuthedFileConnectionData
}

fun createConnectionData(init: ConnectionDataFactory.() -> Unit): ConnectionData {
    val connectionDataFactory: ConnectionDataFactory = ConnectionDataFactory()
    connectionDataFactory.init()
    return connectionDataFactory.create()
}