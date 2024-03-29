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
package eu.vironlab.vextension.item.builder


import eu.vironlab.vextension.builder.Builder
import eu.vironlab.vextension.bukkit.VextensionBukkit
import eu.vironlab.vextension.concurrent.TriConsumer
import eu.vironlab.vextension.item.InteractType
import eu.vironlab.vextension.item.ItemStack
import eu.vironlab.vextension.item.Material
import eu.vironlab.vextension.util.ServerType
import eu.vironlab.vextension.util.ServerUtil
import eu.vironlab.vextension.utils.StringUtil
import java.util.*
import java.util.function.BiConsumer

class ItemBuilder(
    private var material: Material
) : Builder<ItemStack> {

    private var name: String = material.toString().toLowerCase()
    private var amount: Int = 1
    private var damage: Int = 0
    private var lore: MutableList<String> = mutableListOf()
    private var unbreakable: Boolean = false
    private var blockInteract: Boolean = false
    private var blockClick: Boolean = false
    private var blockDrop: Boolean = false
    private var dropHandler: BiConsumer<ItemStack, UUID>? = null
    private var interactHandler: TriConsumer<ItemStack, UUID, InteractType>? = null
    private var clickHandler: BiConsumer<ItemStack, UUID>? = null

    override fun build(): ItemStack {
        var key: String = StringUtil.randomString(64)
        when (ServerUtil.getServerType()) {
            ServerType.SPONGE -> {
                TODO("MAKEN")
            }
            ServerType.BUKKIT -> {
                while (VextensionBukkit.instance.items.containsKey(key)) {
                    key = StringUtil.randomString(64)
                }
            }
        }
        return ItemStack(
            material,
            name,
            amount,
            damage,
            lore,
            unbreakable,
            blockDrop,
            blockInteract,
            blockClick,
            key,
            dropHandler,
            interactHandler,
            clickHandler
        )
    }

    fun build(key: String): ItemStack {
        return ItemStack(
            material,
            name,
            amount,
            damage,
            lore,
            unbreakable,
            blockDrop,
            blockInteract,
            blockClick,
            key,
            dropHandler,
            interactHandler,
            clickHandler
        )
    }

    fun setName(name: String): ItemBuilder {
        this.name = name
        return this
    }

    fun setAmount(amount: Int): ItemBuilder {
        this.amount = amount
        return this
    }

    fun setDamage(damage: Int): ItemBuilder {
        this.damage = damage
        return this
    }

    fun setLore(lore: MutableList<String>): ItemBuilder {
        this.lore = lore
        return this
    }

    fun setUnbreakable(unbreakable: Boolean): ItemBuilder {
        this.unbreakable = unbreakable
        return this
    }

    fun setBlockInteract(blockInteract: Boolean): ItemBuilder {
        this.blockInteract = blockInteract
        return this
    }

    fun setBlockClick(blockClick: Boolean): ItemBuilder {
        this.blockClick = blockClick
        return this
    }

    fun setBlockDrop(blockDrop: Boolean): ItemBuilder {
        this.blockDrop = blockDrop
        return this
    }

    fun setInteractHandler(interactHandler: TriConsumer<ItemStack, UUID, InteractType>?): ItemBuilder {
        this.interactHandler = interactHandler
        return this
    }

    fun setClickHandler(clickHandler: BiConsumer<ItemStack, UUID>?): ItemBuilder {
        this.clickHandler = clickHandler
        return this
    }

    fun setDropHandler(dropHandler: BiConsumer<ItemStack, UUID>?): ItemBuilder {
        this.dropHandler = dropHandler
        return this
    }
    fun setBlockAll(blockAll: Boolean): ItemBuilder {
        this.blockDrop = blockAll
        this.blockInteract = blockAll
        this.blockClick = blockAll
        return this
    }
}

fun item(material: Material, init: ItemBuilder.() -> Unit): ItemStack {
    val itemBuilder = ItemBuilder(material)
    itemBuilder.init()
    return itemBuilder.build()
}