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

package eu.vironlab.vextension.rest.wrapper.mojang

import eu.vironlab.vextension.database.Database
import eu.vironlab.vextension.database.DatabaseClient
import eu.vironlab.vextension.document.document
import eu.vironlab.vextension.extension.toUUID
import eu.vironlab.vextension.rest.wrapper.mojang.user.MojangUser
import java.util.*


class CachedMojangWrapper(val dbClient: DatabaseClient) : DefaultMojangWrapper() {

    val nameUUIDCache: Database = dbClient.getDatabase("mojang_cache_name_uuid").complete()
    val userCache: Database = dbClient.getDatabase("mojang_cache_user").complete()

    override fun getUUID(name: String): Optional<UUID> {
        return Optional.ofNullable(
            nameUUIDCache.get(name.toLowerCase()).complete()?.getString("uuid")?.toUUID() ?: run {
                val rs = super.getUUID(name)
                if (!rs.isPresent) {
                    null
                } else {
                    nameUUIDCache.insert(name.toLowerCase(), document("name", name).append("uuid", rs.get())).complete()
                    rs.get()
                }
            }
        )
    }

    override fun getPlayer(uuid: UUID): Optional<MojangUser> {
        return Optional.ofNullable(
            userCache.get(uuid.toString()).complete()?.toInstance(MojangUser.TYPE) ?: run {
                val rs = super.getPlayer(uuid)
                if (!rs.isPresent) {
                    null
                } else {
                    nameUUIDCache.insert(uuid.toString(), document(rs.get())).complete()
                    rs.get()
                }
            }
        )
    }


}