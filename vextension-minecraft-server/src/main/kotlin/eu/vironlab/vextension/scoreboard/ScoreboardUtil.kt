package eu.vironlab.vextension.scoreboard

import eu.vironlab.vextension.collection.DataPair
import org.bukkit.ChatColor

object ScoreboardUtil {

    @JvmStatic
    fun splitContent(input: String): DataPair<String, String> {
        if (input.length > 11) {
            val first = input.substring(0, 10)
            val second = ChatColor.getLastColors(first) + input.substring(11)
            return DataPair(first, second)
        } else {
            return DataPair(input, "§f")
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