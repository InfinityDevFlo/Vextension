package eu.vironlab.vextension.item.builder

import com.google.gson.JsonParser
import eu.vironlab.vextension.bukkit.VextensionBukkit
import eu.vironlab.vextension.extension.random
import eu.vironlab.vextension.factory.Factory
import eu.vironlab.vextension.item.InteractType
import eu.vironlab.vextension.item.ItemStack
import eu.vironlab.vextension.item.ItemStackLike
import eu.vironlab.vextension.item.Material
import eu.vironlab.vextension.sponge.VextensionSponge
import eu.vironlab.vextension.util.ServerType
import eu.vironlab.vextension.util.ServerUtil
import net.kyori.adventure.text.Component
import java.util.*

class DynamicItemFactory(
    private var material: (UUID?) -> Material
) : Factory<ItemStack>, ItemStackLike {
    private var name: ((UUID?) -> Component?) = { null }
    private var amount: ((UUID?) -> Int) = { 1 }
    private var damage: ((UUID?) -> Int) = { 0 }
    private var lore: ((UUID?) -> MutableList<Component>) = { mutableListOf() }
    private var unbreakable: ((UUID?) -> Boolean) = { false }
    private var blockInteract: ((UUID?) -> Boolean) = { false }
    private var blockClick: ((UUID?) -> Boolean) = { false }
    private var blockDrop: ((UUID?) -> Boolean) = { false }
    private var dropHandler: ((ItemStack, UUID) -> Unit)? = null
    private var interactHandler: ((ItemStack, UUID, Optional<InteractType>) -> Unit)? = null
    private var clickHandler: ((ItemStack, UUID) -> Unit)? = null
    private var permission: ((UUID?) -> String?) = { null }
    private var skullOwner: ((UUID?) -> UUID?) = { null }
    private var skullTexture: ((UUID?) -> String?) = { null }
    private var properties: ((UUID?) -> MutableMap<String, String>) = { mutableMapOf() }

    fun create(uuid: UUID?): ItemStack {
        @Suppress("DuplicatedCode") var key: String = String.random(64)
        when (ServerUtil.SERVER_TYPE) {
            ServerType.SPONGE -> {
                while (VextensionSponge.instance.items.containsKey(key))
                    key = String.random(64)
            }
            ServerType.BUKKIT -> {
                while (VextensionBukkit.items.containsKey(key))
                    key = String.random(64)
            }
        }
        return ItemStack(
            material.invoke(uuid),
            name.invoke(uuid),
            amount.invoke(uuid),
            damage.invoke(uuid),
            lore.invoke(uuid),
            unbreakable.invoke(uuid),
            blockDrop.invoke(uuid),
            blockInteract.invoke(uuid),
            blockClick.invoke(uuid),
            key,
            dropHandler,
            interactHandler,
            clickHandler,
            permission.invoke(uuid),
            skullOwner.invoke(uuid),
            skullTexture.invoke(uuid)?.let {
                when {
                    "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?\$".toRegex().matches(it) -> it
                    try {
                        JsonParser().parse(it);
                        true
                    } catch (e: Exception) {
                        false
                    } -> Base64.getEncoder().encode(it.toByteArray()).decodeToString()
                    else -> Base64.getEncoder().encode("{\"textures\": {\"SKIN\": {\"url\": \"$it\"}}}".toByteArray())
                        .decodeToString()
                }
            },
            properties.invoke(uuid)
        )
    }
    
    override fun create(): ItemStack = create(null)
    fun name(name: (UUID?) -> Component): DynamicItemFactory {
        this.name = name
        return this
    }

    fun skull(uuid: (UUID?) -> UUID): DynamicItemFactory {
        this.skullOwner = uuid
        return this
    }

    fun amount(amount: (UUID?) -> Int): DynamicItemFactory {
        this.amount = amount
        return this
    }

    fun damage(damage: (UUID?) -> Int): DynamicItemFactory {
        this.damage = damage
        return this
    }

    fun lore(lore: (UUID?) -> MutableList<Component>): DynamicItemFactory {
        this.lore = lore
        return this
    }

    fun unbreakable(unbreakable: (UUID?) -> Boolean): DynamicItemFactory {
        this.unbreakable = unbreakable
        return this
    }

    fun blockInteract(blockInteract: (UUID?) -> Boolean): DynamicItemFactory {
        this.blockInteract = blockInteract
        return this
    }

    fun blockClick(blockClick: (UUID?) -> Boolean): DynamicItemFactory {
        this.blockClick = blockClick
        return this
    }

    fun blockDrop(blockDrop: (UUID?) -> Boolean): DynamicItemFactory {
        this.blockDrop = blockDrop
        return this
    }

    fun interactHandler(interactHandler: ((ItemStack, UUID, Optional<InteractType>) -> Unit)?): DynamicItemFactory {
        this.interactHandler = interactHandler
        return this
    }

    fun clickHandler(clickHandler: ((ItemStack, UUID) -> Unit)?): DynamicItemFactory {
        this.clickHandler = clickHandler
        return this
    }

    fun dropHandler(dropHandler: ((ItemStack, UUID) -> Unit)?): DynamicItemFactory {
        this.dropHandler = dropHandler
        return this
    }

    fun blockAll(blockAll: (UUID?) -> Boolean): DynamicItemFactory {
        this.blockDrop = blockAll
        this.blockInteract = blockAll
        this.blockClick = blockAll
        return this
    }

    fun permission(permission: (UUID?) -> String?): DynamicItemFactory {
        this.permission = permission
        return this
    }

    fun properties(properties: (UUID?) -> MutableMap<String, String>): DynamicItemFactory {
        this.properties = properties
        return this
    }

    override fun get(uuid: UUID?): ItemStack = create(uuid)
}
fun dynamicItem(material: (UUID?) -> Material, init: DynamicItemFactory.() -> Unit): DynamicItemFactory {
    return DynamicItemFactory(material).apply(init)
}