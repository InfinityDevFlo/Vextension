package eu.vironlab.vextension.inventory.sponge

import eu.vironlab.vextension.inventory.entry.Page
import eu.vironlab.vextension.inventory.gui.DataGUI
 import eu.vironlab.vextension.inventory.gui.GUI
import eu.vironlab.vextension.item.ItemStack

class SpongePage : Page {
    override fun create(items: MutableList<ItemStack>, page: Int, dataInst: DataGUI, layoutInst: GUI): GUI {
        TODO()
        return layoutInst.also {
            for (i in items) {
                i.clickHandler = dataInst.clickHandler
                //it.addItem(i)
            }
        }
    }
}