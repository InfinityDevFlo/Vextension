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
package eu.vironlab.vextension.inventory.bukkit

import eu.vironlab.vextension.concurrent.task.queueTask
import eu.vironlab.vextension.inventory.gui.DataGUI
import eu.vironlab.vextension.inventory.gui.GUI
import eu.vironlab.vextension.item.ItemStack
import eu.vironlab.vextension.item.ItemStackLike
import eu.vironlab.vextension.item.Material
import eu.vironlab.vextension.item.builder.createItem
import net.kyori.adventure.text.Component
import java.util.*

class BukkitDataGUI(override val lines: Int, override val name: Component) : DataGUI {
    override var comparator: Comparator<ItemStack>? = null
    override var border: ItemStackLike? = null
    override var defaultList: MutableCollection<ItemStackLike> = mutableListOf()
    override var clickHandler: ((ItemStack, UUID) -> Unit)? = null
    override var layout: MutableMap<Int, ItemStackLike> = mutableMapOf()

    override fun open(player: UUID) {
        open(player, defaultList)
    }

    override fun open(player: UUID, list: MutableCollection<ItemStackLike>) {
        open(player, list, comparator)
    }

    init {
        if (lines == 1) {
            throw IllegalArgumentException("Lines have to be higher than 1")
        }
    }

    override fun open(player: UUID, list: MutableCollection<ItemStackLike>, comparator: Comparator<ItemStack>?) {
        queueTask {
            val contents =
                list.map { it.get(player) }.sortedWith(comparator ?: this.comparator ?: throw NullPointerException("Comparator cannot be null")).toMutableList()
            val layout = BukkitGUI(lines, name).also { it.contents = layout.toMutableMap() }
            if (border != null)
                layout.setBorder(border!!.get(player))
            val pages: MutableList<BukkitGUI> = mutableListOf()
            val steps: Int = 9 * lines - layout.contents.size
            //if (border != null) (((lines - 1) * 9) - (lines * 2)) + lines * 2 - 4 - 9 else (lines - 1) * 9
            var step = 0
            var index = 0
            while (step < contents.size) {
                val currentStep = steps * index
                contents.toMutableList().subList(
                    currentStep,
                    if (currentStep + steps < contents.size) currentStep + steps else contents.size
                )
                pages.add(
                    BukkitPage().create(
                        contents.toMutableList().subList(
                            currentStep,
                            if (currentStep + steps < contents.size) currentStep + steps else contents.size,
                        ), index, this, layout
                    )
                )
                index++
                step += steps + 1
            }
            for ((i, page) in pages.withIndex()) {
                val indexUp: (ItemStack, UUID) -> Unit = { _, uuid ->
                    pages[i + 1].open(uuid)
                }
                val indexDown: (ItemStack, UUID) -> Unit = { _, uuid ->
                    pages[i - 1].open(uuid)
                }
                when (i) {
                    0 -> {
                        if (pages.size != 1)
                            page.setItem(lines * 9 - 1, createItem(Material.ARROW) {
                                setName(Component.text("Goto Page ${i + 2} ->"))
                                setBlockAll(true)
                                setClickHandler(indexUp)
                            })
                    }
                    pages.lastIndex -> {
                        page.setItem((lines - 1) * 9, createItem(Material.ARROW) {
                            setName(Component.text("<- Goto Page $i"))
                            setBlockAll(true)
                            setClickHandler(indexDown)
                        })
                    }
                    else -> {
                        page.setItem(lines * 9 - 1, createItem(Material.ARROW) {
                            setName(Component.text("Goto Page ${i + 2} ->"))
                            setBlockAll(true)
                            setClickHandler(indexUp)
                        })
                        page.setItem((lines - 1) * 9, createItem(Material.ARROW) {
                            setName(Component.text("<- Goto Page $i"))
                            setBlockAll(true)
                            setClickHandler(indexDown)
                        })
                    }
                }
            }
            println("OPENING")
            pages[0].open(player)
        }.onError { it.printStackTrace() }.queue()
    }

    fun setComparator(comparator: Comparator<ItemStack>): BukkitDataGUI {
        this.comparator = comparator
        return this
    }

    override fun setBorder(border: ItemStackLike?): BukkitDataGUI {
        this.border = border
        return this
    }

    fun setDefaultList(list: MutableCollection<ItemStackLike>): BukkitDataGUI {
        this.defaultList = list
        return this
    }

    fun setClickHandler(handler: ((ItemStack, UUID) -> Unit)?): BukkitDataGUI {
        this.clickHandler = handler
        return this
    }
    fun setLayoutItem(slot: Int, item: ItemStackLike): BukkitDataGUI {
        this.layout[slot] = item
        return this
    }
    fun addLayoutItem(item: ItemStackLike): BukkitDataGUI {
        var current = 0
        var slot: Int? = null
        for (i in layout.keys.toSortedSet().iterator()) {
            if (i != current) {
                slot = current
                break
            }
            current++
        }
        slot ?: return this
        layout.putIfAbsent(slot, item)
        return this
    }

    fun addAllLayoutItems(items: Map<Int, ItemStackLike>): BukkitDataGUI {
        layout.putAll(items)
        return this
    }
}

fun applyBukkitGUI(gui: BukkitGUI, init: BukkitGUI.() -> BukkitGUI): BukkitGUI {
    gui.init()
    return gui
}