/**
 *   Copyright © 2020 | vironlab.eu | Licensed under the GNU General Public license Version 3<p>
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

package eu.vironlab.vextension.mojang

import eu.vironlab.vextension.concurrent.network.NetworkAction
import eu.vironlab.vextension.rest.wrapper.mojang.MojangServiceStatusList
import eu.vironlab.vextension.mojang.user.MojangUser
import eu.vironlab.vextension.mojang.user.NameHistory
import java.util.*

interface MojangWrapper {

    /**
     * Get the status of all Mojang API Services
     *
     * @return a List with all Services
     */
    fun getServiceStatus(): NetworkAction<MojangServiceStatusList>

    /**
     * Get a MojangUser by [name]
     *
     * @return an Optional with the User
     */
    //fun getPlayer(name: String): NetworkAction<MojangUser?> = getUUID(name)

    /**
     * Get the UUID of a Player by [name]
     *
     * @return an Optional with the UUID
     */
    fun getUUID(name: String): NetworkAction<UUID?>

    /**
     * Get a MojangUser by the [uuid] of a Player
     *
     * @return an Optional with the MojangUser
     */
    fun getPlayer(uuid: UUID): NetworkAction<MojangUser?>

    /**
     * Get the namehistory of [uuid]
     *
     * @return the history as Optional
     */
    fun getNameHistory(uuid: UUID): NetworkAction<NameHistory?>
}