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

package eu.vironlab.vextension.item.extension

import com.google.common.primitives.Booleans
import eu.vironlab.vextension.bukkit.VextensionBukkit
import eu.vironlab.vextension.item.ItemStack
import eu.vironlab.vextension.item.builder.ItemBuilder
import eu.vironlab.vextension.util.ServerType
import eu.vironlab.vextension.util.ServerUtil
import eu.vironlab.vextension.util.UnsupportedServerTypeException
import org.apache.commons.lang.RandomStringUtils
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Vex
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.lang.UnsupportedOperationException


fun ItemStack.toBukkit(): org.bukkit.inventory.ItemStack {
    if (ServerUtil.getServerType() != ServerType.BUKKIT)
        throw UnsupportedServerTypeException("Only usable with bukkit")
    val item: org.bukkit.inventory.ItemStack =
        org.bukkit.inventory.ItemStack(Material.valueOf(this.material.toString()), this.amount)
    val meta = item.itemMeta
    if (meta is Damageable) meta.damage = this.damage
    if (material.name.toLowerCase() != name)
        meta.setDisplayName(this.name)
    else
        meta.setDisplayName(null)
    meta.lore = this.lore
    meta.isUnbreakable = this.unbreakable
    meta.persistentDataContainer.set(VextensionBukkit.key, PersistentDataType.STRING, this.identifier)
    if (!VextensionBukkit.instance.items.containsKey(this.identifier)) {
        VextensionBukkit.instance.items[this.identifier] = this
    }
    /*if (this.blockInteract) meta.persistentDataContainer.set(
        NamespacedKey(VextensionBukkit.instance, "blockInteract"),
        PersistentDataType.STRING,
        "true"
    )
    if (this.blockClick) meta.persistentDataContainer.set(
        NamespacedKey(VextensionBukkit.instance, "blockClick"),
        PersistentDataType.STRING,
        "true"
    )
    if (this.blockDrop) meta.persistentDataContainer.set(
        NamespacedKey(VextensionBukkit.instance, "blockDrop"),
        PersistentDataType.STRING,
        "true"
    )
    var key: NamespacedKey = NamespacedKey(VextensionBukkit.instance, RandomStringUtils.random(64))
    while (VextensionBukkit.instance.interactHandler.containsKey(key)) key = NamespacedKey(VextensionBukkit.instance, RandomStringUtils.random(64))
    if (this.dropHandler != null) {
        meta.persistentDataContainer.set(
            NamespacedKey(VextensionBukkit.instance, "dropHandler"),
            PersistentDataType.STRING,
            key.key
        )
        VextensionBukkit.instance.dropHandler[key] = this.dropHandler
    }
    if (this.interactHandler != null) {
        meta.persistentDataContainer.set(
            NamespacedKey(VextensionBukkit.instance, "interactHandler"),
            PersistentDataType.STRING,
            key.key
        )
        VextensionBukkit.instance.interactHandler[key] = this.interactHandler

    }
    if (this.clickHandler != null) {
        meta.persistentDataContainer.set(
            NamespacedKey(VextensionBukkit.instance, "clickHandler"),
            PersistentDataType.STRING,
            key.key
        )
        VextensionBukkit.instance.clickHandler[key] = this.clickHandler
    }*/
    item.itemMeta = meta
    return item
}

fun ItemStack.toSponge(): ItemStack? {
    TODO()
}

fun org.bukkit.inventory.ItemStack.toItemStack(): ItemStack {
    return VextensionBukkit.instance.items[this.itemMeta.persistentDataContainer.get(
        VextensionBukkit.key,
        PersistentDataType.STRING
    )]  ?: throw UnsupportedOperationException("Invalid Vextension ItemStack")
    //ItemStack(
    /*eu.vironlab.vextension.item.Material.valueOf(this.type.name),
    this.itemMeta.displayName,
    this.amount,
    if (this is Damageable) this.damage else 0,
    this.itemMeta.lore ?: mutableListOf(),
    this.itemMeta.isUnbreakable,
    null,
    null,
    null,
    this.itemMeta.persistentDataContainer.get(VextensionBukkit.key, PersistentDataType.STRING) ?: throw UnsupportedOperationException("Invalid Vextension ItemStack"),
    VextensionBukkit.instance.items[this.itemMeta.persistentDataContainer[VextensionBukkit.key, PersistentDataType.STRING]]?.dropHandler,
    VextensionBukkit.instance.items[this.itemMeta.persistentDataContainer[VextensionBukkit.key, PersistentDataType.STRING]]?.interactHandler,
    VextensionBukkit.instance.items[this.itemMeta.persistentDataContainer[VextensionBukkit.key, PersistentDataType.STRING]]?.clickHandler*/
    //)

}