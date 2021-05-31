package eu.vironlab.vextension.inventory.sponge

import eu.vironlab.vextension.inventory.entry.Page
import eu.vironlab.vextension.inventory.gui.DataGUI
import eu.vironlab.vextension.item.ItemStack

class SpongePage : Page {
    override var border: ItemStack? = null

    override fun create(items: MutableList<ItemStack>, page: Int, dataInst: DataGUI): SpongeGUI {
        return SpongeGUI(dataInst.lines, "${dataInst.name} - #${page + 1}").setBorder(border).also {
            for (i in items) {
                i.clickHandler = dataInst.clickHandler
                it.addItem(i)
            }
        }
    }
}