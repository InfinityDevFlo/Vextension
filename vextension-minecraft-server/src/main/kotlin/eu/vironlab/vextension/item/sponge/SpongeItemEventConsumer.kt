package eu.vironlab.vextension.item.sponge

class SpongeItemEventConsumer {
    /*@Listener
    fun interactListener(event: org.spongepowered.api.event.item.inventory.InteractItemEvent) {
        try {
            event.itemStack.toItemStack().let {
                if (it.blockInteract)
                    event.isCancelled = true
                it.interactHandler?.invoke(it, (event.source as Player).uniqueId, Optional.empty())
            }
        } catch (e: Exception) {
        }
    }

    @Listener
    fun dropListener(event: DropItemEvent) {
        if (event.source !is Player) return
        try {
            event.context.get(EventContextKeys.USED_ITEM).get().toItemStack().let {
                if (it.blockDrop) event.isCancelled = true
                it.dropHandler?.invoke(it, (event.source as Player).uniqueId)
            }
        } catch (e: Exception) {
        }
    }

    @Listener
    fun clickListener(event: org.spongepowered.api.event.item.inventory.ClickInventoryEvent) {
        if (event.source !is Player) return
        event.transactions.forEach { slot ->
            slot.custom.ifPresent { itt ->
                try {
                    itt.toItemStack().let {
                        if (it.blockInteract) event.isCancelled = true
                        it.clickHandler?.invoke(it, (event.source as Player).uniqueId)
                    }
                } catch (e: Exception) {
                }
            }
        }
    }*/
}