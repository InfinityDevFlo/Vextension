package eu.vironlab.vextension.inventory.bukkit

import eu.vironlab.vextension.inventory.entry.Page
import eu.vironlab.vextension.inventory.gui.DataGUI
import eu.vironlab.vextension.inventory.gui.GUI
import eu.vironlab.vextension.item.ItemStack

class BukkitPage : Page {

    override fun create(items: MutableList<ItemStack>, page: Int, dataInst: DataGUI, layoutInst: GUI): BukkitGUI {
        layoutInst as BukkitGUI
        return layoutInst.clone().also {
            for (i in items) {
                if (i.clickHandler == null)
                    i.clickHandler = dataInst.clickHandler
                it.addItem(i)
            }
        }
    }

}