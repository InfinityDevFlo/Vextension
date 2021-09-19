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

package eu.vironlab.vextension.item.extension

import com.destroystokyo.paper.profile.PlayerProfile
import com.destroystokyo.paper.profile.ProfileProperty
import com.google.gson.JsonParser
import eu.vironlab.vextension.bukkit.VextensionBukkit
import eu.vironlab.vextension.item.ItemStack
import eu.vironlab.vextension.mojang.AbstractMojangWrapper
import eu.vironlab.vextension.mojang.MojangConstants
import eu.vironlab.vextension.sponge.VextensionSponge
import eu.vironlab.vextension.sponge.VextensionSponge.Companion.vextensionSpongeKey
import eu.vironlab.vextension.util.ServerType
import eu.vironlab.vextension.util.ServerUtil
import eu.vironlab.vextension.util.UnsupportedServerTypeException
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.profile.GameProfile
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import org.bukkit.inventory.ItemStack as BukkitItemStack
import org.spongepowered.api.item.inventory.ItemStack as SpongeItemStack


fun ItemStack.toBukkit(): BukkitItemStack {
    if (ServerUtil.SERVER_TYPE != ServerType.BUKKIT)
        throw UnsupportedServerTypeException("Only usable with bukkit")
    val item =
        BukkitItemStack(Material.valueOf(this.material.toString()), this.amount)
    val meta = item.itemMeta
    if (meta is Damageable) meta.damage = this.damage
    if (name != null)
        meta.displayName(Component.text(this.name!!))
    if (skullOwner != null)
        if (!Bukkit.getOfflinePlayer(skullOwner!!).hasPlayedBefore()) (meta as SkullMeta).playerProfile =
            Bukkit.createProfile(skullOwner!!)
                .also {
                    it.properties.also { l -> l.removeIf { ll -> ll.name == "textures" } }.add(
                        ProfileProperty(
                            "textures",
                            skullTexture ?: JsonParser().parse(
                                InputStreamReader(
                                    URL("https://sessionserver.mojang.com/session/minecraft/profile/$skullOwner").openConnection()
                                        .getInputStream()
                                )
                            )
                                .asJsonObject.get("properties").asJsonArray[0].asJsonObject.get("value").asString/*.let { textureData ->
                        String.format("{textures:{SKIN:{url:\\\"%s\\\"}}}", JsonParser().parse(Base64.getDecoder().decode(textureData).decodeToString())
                            .asJsonObject.getAsJsonObject("textures")
                            .getAsJsonObject("SKIN").get("url").asString)
                }*/
                        )
                    )

                } else
            (meta as SkullMeta).owningPlayer = Bukkit.getOfflinePlayer(skullOwner!!)
    meta.lore(this.lore.map { Component.text(it) })
    meta.isUnbreakable = this.unbreakable
    meta.persistentDataContainer.set(VextensionBukkit.key, PersistentDataType.STRING, this.identifier)
    if (!VextensionBukkit.items.containsKey(this.identifier)) {
        VextensionBukkit.items[this.identifier] = this
    }
    item.itemMeta = meta
    return item
}


/*fun ItemStack.toSponge(): SpongeItemStack {
    return Sponge.getRegistry().createBuilder(Builder::class.java)
        .itemType(DummyObjectProvider.createFor(ItemType::class.java, this.material.name.toUpperCase()))
        .build().also {
            it.get(Keys.ITEM_DURABILITY).ifPresent { itt ->
                it.offer(Keys.ITEM_DURABILITY, itt - damage)
            }
            it.quantity = amount
            if (name != null)
                it.offer(Keys.DISPLAY_NAME, Text.of(name!!))
            it.offer(Keys.UNBREAKABLE, unbreakable)
            it.offer(Keys.ITEM_LORE, lore.map { itt -> Text.of(itt) })
            it.offer(vextensionSpongeKey, this.identifier)
            if (!VextensionSponge.instance.items.containsKey(identifier))
                VextensionSponge.instance.items[identifier] = this
        }
}
*/
fun BukkitItemStack.toItemStack(): ItemStack {
    return VextensionBukkit.items[this.itemMeta.persistentDataContainer.get(
        VextensionBukkit.key,
        PersistentDataType.STRING
    )] ?: throw UnsupportedOperationException("Invalid Vextension ItemStack")
}

fun SpongeItemStack.toItemStack(): ItemStack {
    return VextensionSponge.instance.items[this.get(vextensionSpongeKey)
        .orElseThrow { UnsupportedOperationException("Invalid Vextension ItemStack") }]
        ?: throw UnsupportedOperationException("Invalid Vextension ItemStack")
}

fun ItemStackSnapshot.toItemStack(): ItemStack {
    return VextensionSponge.instance.items[this.get(vextensionSpongeKey)
        .orElseThrow { UnsupportedOperationException("Invalid Vextension ItemStack") }]
        ?: throw UnsupportedOperationException("Invalid Vextension ItemStack")
}