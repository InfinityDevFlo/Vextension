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

import com.google.gson.Gson
import com.google.gson.JsonElement
import eu.vironlab.vextension.extension.toUUID
import eu.vironlab.vextension.rest.wrapper.mojang.user.MojangUser
import eu.vironlab.vextension.rest.wrapper.mojang.user.NameHistory
import eu.vironlab.vextension.rest.wrapper.mojang.user.NameHistoryEntry
import eu.vironlab.vextension.rest.wrapper.mojang.user.Skin
import java.util.*


open class DefaultMojangWrapper : AbstractMojangWrapper() {
    override fun getPlayer(uuid: UUID): Optional<MojangUser> {
        val profileRequest = MojangConstants.CLIENT.getJsonDocument(
            MojangConstants.PLAYER_PROFILE_URL.replace(
                "%uuid%",
                uuid.toString()
            )
        )
        var result: MojangUser? = null
        profileRequest.ifPresent {
            val name = it.getString("name") ?: throw IllegalStateException("There is no Name")
            val properties = it.getJsonArray("properties") ?: throw IllegalStateException("No Properties Given")
            val skin = Gson().fromJson(properties.first().asJsonObject, Skin::class.java)
            val namehistory = getNameHistory(uuid).get()
            result = MojangUser(uuid, name, namehistory, skin)
        }
        return Optional.ofNullable(result)
    }

    override fun getUUID(name: String): Optional<UUID> {
        val request = MojangConstants.CLIENT.getJsonDocument(MojangConstants.UUID_REQUEST_URL + name)
        var result: UUID? = null
        request.ifPresent {
            result = it.getString("id")!!.toUUID()
        }
        return Optional.ofNullable(result)
    }

    override fun getNameHistory(uuid: UUID): Optional<NameHistory> {
        val request =
            MojangConstants.CLIENT.getJsonArray(MojangConstants.PLAYER_NAME_HISTORY.replace("%uuid%", uuid.toString()))
        var result: NameHistory? = null
        request.ifPresent {
            val firstName: JsonElement = it.first()
            it.remove(0)
            val history: MutableList<NameHistoryEntry> =
                mutableListOf(NameHistoryEntry(firstName.asJsonObject.get("name").asString, 0L))
            it.forEach { element ->
                val name = element.asJsonObject.get("name").asString
                val changedToAt = element.asJsonObject.get("changedToAt").asLong
                history.add(NameHistoryEntry(name, changedToAt))
            }
            result = NameHistory(history)
        }
        return Optional.ofNullable(result)
    }

}
