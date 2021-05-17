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
 *   item program is free software: you can redistribute it and/or modify<p>
 *   it under the terms of the GNU General Public License as published by<p>
 *   the Free Software Foundation, either version 3 of the License, or<p>
 *   (at your option) any later version.<p>
 *<p>
 *   item program is distributed in the hope that it will be useful,<p>
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of<p>
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<p>
 *   GNU General Public License for more details.<p>
 *<p>
 *   You should have received a copy of the GNU General Public License<p>
 *   along with item program.  If not, see <http://www.gnu.org/licenses/>.<p>
 *<p>
 *   Contact:<p>
 *<p>
 *     Discordserver:   https://discord.gg/wvcX92VyEH<p>
 *     Website:         https://vironlab.eu/ <p>
 *     Mail:            contact@vironlab.eu<p>
 *<p>
 */

package eu.vironlab.vextension.item.extension

import eu.vironlab.vextension.bukkit.VextensionBukkit
import eu.vironlab.vextension.concurrent.task.queueTask
import eu.vironlab.vextension.item.ItemStack
import eu.vironlab.vextension.util.ServerType
import eu.vironlab.vextension.util.ServerUtil
import eu.vironlab.vextension.util.UnsupportedServerTypeException
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType

/**
 * Set the [item] on [slot] into the Inventory
 */
fun Inventory.setItem(slot: Int, item: ItemStack): Inventory {
    if (ServerUtil.getServerType() != ServerType.BUKKIT)
        throw UnsupportedServerTypeException("Only usable with bukkit")
    queueTask {
        val bukkitItem: org.bukkit.inventory.ItemStack =
            org.bukkit.inventory.ItemStack(Material.valueOf(item.material.toString()), item.amount)
        val meta = bukkitItem.itemMeta
        if (meta is Damageable) meta.damage = item.damage
        if (item.material.name.toLowerCase() != item.name)
            meta.setDisplayName(item.name)
        meta.lore = item.lore
        meta.isUnbreakable = item.unbreakable
        meta.persistentDataContainer.set(VextensionBukkit.key, PersistentDataType.STRING, item.identifier)
        bukkitItem.itemMeta = meta
        if (!VextensionBukkit.items.containsKey(item.identifier)) {
            VextensionBukkit.items[item.identifier] = item
        }
        this.setItem(slot, bukkitItem)
    }.queue()
    return this
}

/**
 * Add the [item] into a Bukkit Inventory
 */
fun Inventory.addItem(item: ItemStack): Inventory {
    this.setItem(this.firstEmpty(), item)
    return this
}