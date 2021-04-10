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
package eu.vironlab.vextension.inventory.bukkit

import eu.vironlab.vextension.concurrent.scheduleAsync
import eu.vironlab.vextension.inventory.gui.DataGUI
import eu.vironlab.vextension.item.ItemStack
import eu.vironlab.vextension.item.Material
import eu.vironlab.vextension.item.builder.item
import org.bukkit.Bukkit
import java.util.*
import java.util.function.BiConsumer

class BukkitDataGUI(override val lines: Int, override val name: String) : DataGUI {
    override var comparator: Comparator<ItemStack>? = null
    override var border: ItemStack? = null
    override var defaultList: MutableCollection<ItemStack> = mutableListOf()
    override fun open(player: UUID) {
        open(player, defaultList)
    }

    init {
        if (lines == 1) {
            throw IllegalArgumentException("Lines have to be higher than 1")
        }
    }

    override fun open(player: UUID, list: MutableCollection<ItemStack>) {
        scheduleAsync {
            val contents =
                list.sortedWith(comparator ?: throw NullPointerException("Comparator cannot be null")).toMutableList()
            val pages: MutableList<BukkitGUI> = mutableListOf()
            val steps: Int = if (border != null) (((lines - 1) * 9) - (lines * 2)) + lines * 2 - 4 - 9 else (lines - 1) * 9
            var step = 0
            var index = 0
            while (step < contents.size) {
                val currentStep = steps * index
                pages.add(BukkitPage().also {
                    it.border = this.border
                }.create(list.toMutableList().subList(currentStep, if (currentStep + steps < list.size) currentStep + steps else list.size), index, this))
                index++
                step += steps
            }
            /*for ((first, second) in (steps*-1..contents.size step steps).withIndex()) {
                pages.add(BukkitPage().also {
                    it.border = this.border
                }.create(list.toMutableList().subList(last, second), first, this))
                last = second + 1
            }*/
            for ((indexx, page) in pages.withIndex()) {
                val indexUp: BiConsumer<ItemStack, UUID> = BiConsumer { _, uuid ->
                    pages[indexx + 1].open(uuid)
                }
                val indexDown: BiConsumer<ItemStack, UUID> = BiConsumer { _, uuid ->
                    pages[indexx - 1].open(uuid)
                }
                when (indexx) {
                    0 -> {
                        page.setItem(lines * 9 - 1, item(Material.ARROW) {
                            setName("Goto Page ${indexx + 2} ->")
                            setBlockAll(true)
                            setClickHandler(indexUp)
                        })
                    }
                    pages.lastIndex -> {
                        page.setItem((lines - 1) * 9, item(Material.ARROW) {
                            setName("<- Goto Page $indexx")
                            setBlockAll(true)
                            setClickHandler(indexDown)
                        })
                    }
                    else -> {
                        page.setItem(lines * 9 - 1, item(Material.ARROW) {
                            setName("Goto Page ${indexx + 2} ->")
                            setBlockAll(true)
                            setClickHandler(indexUp)
                        })
                        page.setItem((lines - 1) * 9, item(Material.ARROW) {
                            setName("<- Goto Page $indexx")
                            setBlockAll(true)
                            setClickHandler(indexDown)
                        })
                    }
                }
            }
            pages[0].open(player)
        }
    }

    fun setComparator(comparator: Comparator<ItemStack>): BukkitDataGUI {
        this.comparator = comparator
        return this
    }

    fun setBorder(border: ItemStack): BukkitDataGUI {
        this.border = border
        return this
    }

    fun setDefaultList(list: MutableCollection<ItemStack>): BukkitDataGUI {
        this.defaultList = list
        return this
    }
}
