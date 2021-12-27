package eu.vironlab.vextension.inventory.sponge

import eu.vironlab.vextension.concurrent.task.queueTask
import eu.vironlab.vextension.inventory.gui.DataGUI
import eu.vironlab.vextension.inventory.gui.GUI
import eu.vironlab.vextension.item.ItemStack
import eu.vironlab.vextension.item.ItemStackLike
import eu.vironlab.vextension.item.Material
import eu.vironlab.vextension.item.builder.createItem
import net.kyori.adventure.text.Component
import java.util.*

class SpongeDataGUI(override val lines: Int, override val name: Component) : DataGUI {
    override var comparator: Comparator<ItemStack>? = null
    override var defaultList: MutableCollection<ItemStackLike> = mutableListOf()
    override var clickHandler: ((ItemStack, UUID) -> Unit)? = null
    override var border: ItemStackLike? = null
    override var layout: MutableMap<Int, ItemStackLike>
        get() = TODO("Not yet implemented")
        set(value) {}

    init {
        if (lines == 1) {
            throw IllegalArgumentException("Lines have to be higher than 1")
        }
    }

    override fun open(player: UUID, list: MutableCollection<ItemStackLike>) {
        open(player, list, comparator)
    }

    override fun open(player: UUID, list: MutableCollection<ItemStackLike>, comparator: Comparator<ItemStack>?) {
        queueTask {
            val contents =
                list.map { it.get(player) }.sortedWith(comparator ?: this.comparator ?: throw NullPointerException("Comparator cannot be null")).toMutableList()
            val pages: MutableList<SpongeGUI> = mutableListOf()
            val steps: Int =
                if (border != null) (((lines - 1) * 9) - (lines * 2)) + lines * 2 - 4 - 9 else (lines - 1) * 9
            var step = 0
            var index = 0
            while (step < contents.size) {
                val currentStep = steps * index
                contents.toMutableList().subList(
                    currentStep,
                    if (currentStep + steps < contents.size) currentStep + steps else contents.size
                )
                pages.add(
                    SpongePage().create(
                        contents.toMutableList().subList(
                            currentStep,
                            if (currentStep + steps < contents.size) currentStep + steps else contents.size
                        ), index, this, SpongeGUI(lines, name)
                    ) as SpongeGUI
                )
                index++
                step += steps + 1
            }
            for ((indexx, page) in pages.withIndex()) {
                val indexUp: (ItemStack, UUID) -> Unit = { _, uuid ->
                    pages[indexx + 1].open(uuid)
                }
                val indexDown: (ItemStack, UUID) -> Unit = { _, uuid ->
                    pages[indexx - 1].open(uuid)
                }
                when (indexx) {
                    0 -> {
                        page.setItem(lines * 9 - 1, createItem(Material.ARROW) {
                            setName(Component.text("Goto Page ${indexx + 2} ->"))
                            setBlockAll(true)
                            setClickHandler(indexUp)
                        })
                    }
                    pages.lastIndex -> {
                        page.setItem((lines - 1) * 9, createItem(Material.ARROW) {
                            setName(Component.text("<- Goto Page $indexx"))
                            setBlockAll(true)
                            setClickHandler(indexDown)
                        })
                    }
                    else -> {
                        page.setItem(lines * 9 - 1, createItem(Material.ARROW) {
                            setName(Component.text("Goto Page ${indexx + 2} ->"))
                            setBlockAll(true)
                            setClickHandler(indexUp)
                        })
                        page.setItem((lines - 1) * 9, createItem(Material.ARROW) {
                            setName(Component.text("<- Goto Page $indexx"))
                            setBlockAll(true)
                            setClickHandler(indexDown)
                        })
                    }
                }
            }
            pages[0].open(player)
        }
    }

    override fun open(player: UUID) = open(player, defaultList)

    override fun setBorder(border: ItemStackLike?): GUI {
        this.border = border
        return this
    }
}