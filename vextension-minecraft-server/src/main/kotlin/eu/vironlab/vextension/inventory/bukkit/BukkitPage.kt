package eu.vironlab.vextension.inventory.bukkit

import eu.vironlab.vextension.inventory.entry.Page
import eu.vironlab.vextension.item.ItemStack

class BukkitPage : Page {
    override var border: ItemStack? = null

    override fun create(items: MutableList<ItemStack>, page: Int, dataInst: BukkitDataGUI): BukkitGUI {
        return BukkitGUI(dataInst.lines, "${dataInst.name} - #${page + 1}").setBorder(border).also {
            for (i in items) {
                i.clickHandler = dataInst.clickHandler
                it.addItem(i)
            }
        }
    }

}