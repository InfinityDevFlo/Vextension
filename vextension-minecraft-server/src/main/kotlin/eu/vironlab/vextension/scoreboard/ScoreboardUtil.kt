package eu.vironlab.vextension.scoreboard

import eu.vironlab.vextension.collection.DataPair
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacy
import org.bukkit.ChatColor

object ScoreboardUtil {
    val serializer = legacy('§')
    @JvmStatic
    fun splitContent(inputComp: Component): DataPair<Component, Component> {
        val input: String = serializer.serialize(inputComp)
        return if (input.length > 64) {
            val first = input.substring(0, 63)
            val second = ChatColor.getLastColors(first) + input.substring(64)
            DataPair(serializer.deserialize(first), serializer.deserialize(second))
        } else {
            DataPair(inputComp, Component.empty())
        }
    }

    @JvmStatic
    fun getAvailableColor(used: Collection<String>): String {
        val codes: MutableList<String> = mutableListOf(
            "§0",
            "§1",
            "§2",
            "§3",
            "§4",
            "§5",
            "§6",
            "§7",
            "§8",
            "§9",
            "§a",
            "§b",
            "§c",
            "§d",
            "§e",
            "§f"
        )
        var result = codes.random()
        while (used.contains(result)) {
            result = codes.random()
        }
        return result
    }

}