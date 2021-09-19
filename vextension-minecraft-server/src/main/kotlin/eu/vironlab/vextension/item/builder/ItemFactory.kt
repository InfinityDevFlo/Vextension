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
package eu.vironlab.vextension.item.builder


import eu.vironlab.vextension.bukkit.VextensionBukkit
import eu.vironlab.vextension.extension.random
import eu.vironlab.vextension.factory.Factory
import eu.vironlab.vextension.item.InteractType
import eu.vironlab.vextension.item.ItemStack
import eu.vironlab.vextension.item.Material
import eu.vironlab.vextension.sponge.VextensionSponge
import eu.vironlab.vextension.util.ServerType
import eu.vironlab.vextension.util.ServerUtil
import java.util.*

class ItemFactory(
    private var material: Material
) : Factory<ItemStack> {

    private var name: String? = null
    private var amount: Int = 1
    private var damage: Int = 0
    private var lore: MutableList<String> = mutableListOf()
    private var unbreakable: Boolean = false
    private var blockInteract: Boolean = false
    private var blockClick: Boolean = false
    private var blockDrop: Boolean = false
    private var dropHandler: ((ItemStack, UUID) -> Unit)? = null
    private var interactHandler: ((ItemStack, UUID, Optional<InteractType>) -> Unit)? = null
    private var clickHandler: ((ItemStack, UUID) -> Unit)? = null
    private var permission: String? = null
    private var skullOwner: UUID? = null
    private var skullTexture: String? = null

    override fun create(): ItemStack {
        var key: String = String.random(64)
        when (ServerUtil.SERVER_TYPE) {
            ServerType.SPONGE -> {
                while (VextensionSponge.instance.items.containsKey(key))
                    key = String.random(64)
            }
            ServerType.BUKKIT -> {
                while (VextensionBukkit.items.containsKey(key))
                    key = String.random(64)
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
            clickHandler,
            permission,
            skullOwner,
            skullTexture
        )
    }

    private class AlreadyExistsException(msg: String) : Throwable(msg)

    fun build(key: String): ItemStack {
        when (ServerUtil.SERVER_TYPE) {
            ServerType.BUKKIT -> {
                if (VextensionBukkit.items.containsKey(key))
                    throw AlreadyExistsException("Item with the same key already exists!")
            }
            ServerType.SPONGE -> {
                if (VextensionSponge.instance.items.containsKey(key))
                    throw AlreadyExistsException("Item with the same key already exists!")
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
            clickHandler,
            permission,
            skullOwner,
            skullTexture
        )
    }

    fun setName(name: String): ItemFactory {
        this.name = name
        return this
    }

    fun setSkullOwner(uuid: UUID): ItemFactory {
        this.skullOwner = uuid
        return this
    }

    fun setAmount(amount: Int): ItemFactory {
        this.amount = amount
        return this
    }

    fun setDamage(damage: Int): ItemFactory {
        this.damage = damage
        return this
    }

    fun setLore(lore: MutableList<String>): ItemFactory {
        this.lore = lore
        return this
    }

    fun setUnbreakable(unbreakable: Boolean): ItemFactory {
        this.unbreakable = unbreakable
        return this
    }

    fun setBlockInteract(blockInteract: Boolean): ItemFactory {
        this.blockInteract = blockInteract
        return this
    }

    fun setBlockClick(blockClick: Boolean): ItemFactory {
        this.blockClick = blockClick
        return this
    }

    fun setBlockDrop(blockDrop: Boolean): ItemFactory {
        this.blockDrop = blockDrop
        return this
    }

    fun setInteractHandler(interactHandler: ((ItemStack, UUID, Optional<InteractType>) -> Unit)?): ItemFactory {
        this.interactHandler = interactHandler
        return this
    }

    fun setClickHandler(clickHandler: ((ItemStack, UUID) -> Unit)?): ItemFactory {
        this.clickHandler = clickHandler
        return this
    }

    fun setDropHandler(dropHandler: ((ItemStack, UUID) -> Unit)?): ItemFactory {
        this.dropHandler = dropHandler
        return this
    }

    fun setBlockAll(blockAll: Boolean): ItemFactory {
        this.blockDrop = blockAll
        this.blockInteract = blockAll
        this.blockClick = blockAll
        return this
    }

    fun setPermission(permission: String?): ItemFactory {
        this.permission = permission
        return this
    }

    fun setFallbackSkullTexture(textureURL: String?): ItemFactory {
        this.skullTexture = textureURL
        return this
    }
}

fun createItem(material: Material, init: ItemFactory.() -> Unit): ItemStack {
    val itemFactory = ItemFactory(material)
    itemFactory.init()
    return itemFactory.create()
}