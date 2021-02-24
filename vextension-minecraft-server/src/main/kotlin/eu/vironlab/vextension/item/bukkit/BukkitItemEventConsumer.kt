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

package eu.vironlab.vextension.item.bukkit

import eu.vironlab.vextension.bukkit.VextensionBukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*
import org.bukkit.event.player.*
import org.bukkit.persistence.PersistentDataType

class BukkitItemEventConsumer : Listener {
    @EventHandler
    fun interact(e: PlayerInteractEvent) {
        if (e.item != null) {
            if (e.item!!.hasItemMeta()) {
                if (!e.item!!.itemMeta.persistentDataContainer.isEmpty) {
                    val item =
                        VextensionBukkit.instance.items[e.item!!.itemMeta.persistentDataContainer[VextensionBukkit.key, PersistentDataType.STRING]]
                            ?: return
                    if (item.blockInteract) e.isCancelled = true
                    if (item.interactHandler != null) item.interactHandler!!.accept(item, e.player.uniqueId)
                }
            }
        }
    }

    @EventHandler
    fun drop(e: PlayerDropItemEvent) {
        if (e.itemDrop.itemStack.hasItemMeta()) {
            if (!e.itemDrop.itemStack.itemMeta.persistentDataContainer.isEmpty) {
                val item =
                    VextensionBukkit.instance.items[e.itemDrop.itemStack.itemMeta.persistentDataContainer[VextensionBukkit.key, PersistentDataType.STRING]]
                        ?: return
                if (item.blockDrop) e.isCancelled = true
                if (item.dropHandler != null) item.dropHandler!!.accept(item, e.player.uniqueId)
            }
        }
    }

    @EventHandler
    fun click(e: InventoryClickEvent) {
        if (e.action == InventoryAction.NOTHING)
            return
        var item =
            if (e.hotbarButton != -1) e.whoClicked.inventory.getItem(e.hotbarButton) else null
                ?: if (e.cursor?.type != org.bukkit.Material.AIR) e.cursor else null
                    ?: if (e.currentItem?.type != org.bukkit.Material.AIR) e.currentItem else null
                        ?: return
        if (VextensionBukkit.instance.items[item?.itemMeta?.persistentDataContainer?.get(
                VextensionBukkit.key,
                PersistentDataType.STRING
            )] == null
        ) {
            item = when (item) {
                e.currentItem -> if (e.currentItem?.type != org.bukkit.Material.AIR) e.cursor else e.clickedInventory?.getItem(
                    e.hotbarButton
                ) ?: return
                e.cursor -> if (e.cursor?.type != org.bukkit.Material.AIR) e.currentItem else e.clickedInventory?.getItem(
                    e.hotbarButton
                ) ?: return
                else -> return
            }
        }
        if (item != null) {
            if (item.hasItemMeta()) {
                if (!item.itemMeta.persistentDataContainer.isEmpty) {
                    val itemm: eu.vironlab.vextension.item.ItemStack =
                        VextensionBukkit.instance.items[item.itemMeta.persistentDataContainer[VextensionBukkit.key, PersistentDataType.STRING]]
                            ?: return
                    if (itemm.blockClick) e.isCancelled = true
                    if (itemm.clickHandler != null) itemm.clickHandler!!.accept(itemm, e.whoClicked.uniqueId)
                }
            }
        }
    }

}