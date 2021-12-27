package eu.vironlab.vextension.item

import java.util.*

interface ItemStackLike {
    fun get(uuid: UUID?): ItemStack
}