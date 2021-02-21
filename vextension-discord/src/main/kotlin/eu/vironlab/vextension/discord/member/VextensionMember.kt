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

package eu.vironlab.vextension.discord.member

import com.google.gson.JsonArray
import eu.vironlab.vextension.database.update
import eu.vironlab.vextension.discord.DiscordUtil
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.document.DocumentManagement
import java.util.concurrent.TimeUnit
import net.dv8tion.jda.api.entities.Member


class VextensionMember(member: Member) : Member by member {

    var properties: Document = DiscordUtil.userDatabase.getOrDefault(this.id, DocumentManagement.newDocument(this.id))
        .getDocument(this.guild.id, DocumentManagement.newDocument(this.guild.id))
    //val permissions: MutableList<MemberPermissionInfo> =
        //properties.getList("permissions", mutableListOf())

    /*fun hasPermission(permission: String): Boolean {
        val info = permissions.first {
            it.permission == permission
        }
        if (info.timeout == 0L) {
            return true
        }else {
            if(System.currentTimeMillis() > info.timeout) {
                permissions.remove(info)
                update()
                return false
            }else {
                return true
            }
        }
    }

    fun addPermission(permission: String) = addPermission(permission, 0)

    fun addPermission(permission: String, timeout: Long, timeUnit: TimeUnit) = addPermission(permission, timeUnit.toMillis(timeout))

    fun addPermission(permission: String, timeout: Long) {
        this.permissions.add(MemberPermissionInfo(permission, timeout))
    }

    fun update() {
        this.properties.delete("permissions")
        this.properties.insert("permissions", permissions)
        DiscordUtil.userDatabase.update(this.id, properties)
    }*/

}

fun Member.toVextension(): VextensionMember = VextensionMember(this)