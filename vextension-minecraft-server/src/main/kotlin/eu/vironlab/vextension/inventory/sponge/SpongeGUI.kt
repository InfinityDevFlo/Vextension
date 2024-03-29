package eu.vironlab.vextension.inventory.sponge

import eu.vironlab.vextension.concurrent.task.queueTask
import eu.vironlab.vextension.inventory.gui.GUI
import eu.vironlab.vextension.item.ItemStack
import eu.vironlab.vextension.item.ItemStackLike
import eu.vironlab.vextension.util.ServerType
import eu.vironlab.vextension.util.ServerUtil
import eu.vironlab.vextension.util.UnsupportedServerTypeException
import net.kyori.adventure.text.Component
import java.util.*

class SpongeGUI(override val lines: Int, override val name: Component) : GUI {
    var contents: MutableMap<Int, ItemStackLike> = mutableMapOf()
    override fun open(player: UUID) {
        if (ServerUtil.SERVER_TYPE != ServerType.SPONGE)
            throw UnsupportedServerTypeException("SpongeGUI only supports Sponge!")
        queueTask {
            /*ViewableInventory.builder().type(ContainerTypes.)
            Inventory.builder().slots(9 * lines).completeStructure().build().
                .property("title", InventoryTitle(Text.of(name)))
                .property("inventorydimensions", InventoryDimension(9, 9 * lines))
                .build(VextensionSponge.instance)
                .also {
                    val spongePlayer =
                        Sponge.server().player(player).orElseThrow { NullPointerException("Invalid Player") }
                    for (item in contents) {
                        if (item.value.permission != null)
                            if (spongePlayer.hasPermission(item.value.permission!!))
                                it.query<Inventory>(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(item.key)))
                                    .first<Inventory>()
                                    .set(item.value.toSponge())
                    }
                    spongePlayer.openInventory(it)
                }*/
        }.queue()
    }

    override fun setBorder(border: ItemStackLike?): SpongeGUI {
        //<editor-fold desc="Border creation" defaultstate="collapsed">
        for (i: Int in 0..8) {
            if (border != null)
                contents[i] = border
            else contents.remove(i)
        }
        for (i: Int in lines * 9 - 9 until lines * 9) {
            if (border != null)
                contents[i] = border
            else contents.remove(i)
        }
        var i = 9
        while (i < 9 * lines) {
            if (border != null)
                contents[i] = border
            else contents.remove(i)
            if (i % 9 == 0) i += 8
            else i++
        }
        //</editor-fold>
        return this
    }

    fun setItem(slot: Int, item: ItemStack): SpongeGUI {
        contents[slot] = item
        return this
    }

    fun removeItem(slot: Int): SpongeGUI {
        contents.remove(slot)
        return this
    }


    fun addAllItems(itemList: Map<out Int, ItemStack>): SpongeGUI {
        this.contents.putAll(itemList)
        return this
    }

    fun addItem(item: ItemStack): SpongeGUI {
        var current = 0
        var slot: Int? = null
        for (i in contents.keys.toSortedSet().iterator()) {
            if (i != current++) {
                slot = current.minus(1)
                break
            }
        }
        slot ?: return this
        println("$slot ${contents.containsKey(slot)}")
        contents.putIfAbsent(slot, item)
        return this
    }
}