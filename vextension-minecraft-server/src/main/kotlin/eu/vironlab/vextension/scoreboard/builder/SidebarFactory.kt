/**
 *   Copyright © 2020 | vironlab.eu | All Rights Reserved.<p>
 * <p>
 *      ___    _______                        ______         ______  <p>
 *      __ |  / /___(_)______________ _______ ___  / ______ ____  /_ <p>
 *      __ | / / __  / __  ___/_  __ \__  __ \__  /  _  __ `/__  __ \<p>
 *      __ |/ /  _  /  _  /    / /_/ /_  / / /_  /___/ /_/ / _  /_/ /<p>
 *      _____/   /_/   /_/     \____/ /_/ /_/ /_____/\__,_/  /_.___/ <p>
 *<p>
 *    ____  _______     _______ _     ___  ____  __  __ _____ _   _ _____ <p>
 *   |  _ \| ____\ \   / / ____| |   / _ \|  _ \|  \/  | ____| \ | |_   _|<p>
 *   | | | |  _|  \ \ / /|  _| | |  | | | | |_) | |\/| |  _| |  \| | | |  <p>
 *   | |_| | |___  \ V / | |___| |__| |_| |  __/| |  | | |___| |\  | | |  <p>
 *   |____/|_____|  \_/  |_____|_____\___/|_|   |_|  |_|_____|_| \_| |_|  <p>
 *<p>
 *<p>
 *   This program is free software: you can redistribute it and/or modify<p>
 *   it under the terms of the GNU General Public License as published by<p>
 *   the Free Software Foundation, either version 3 of the License, or<p>
 *   (at your option) any later version.<p>
 *<p>
 *   This program is distributed in the hope that it will be useful,<p>
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of<p>
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<p>
 *   GNU General Public License for more details.<p>
 *<p>
 *   You should have received a copy of the GNU General Public License<p>
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.<p>
 *<p>
 *   Contact:<p>
 *<p>
 *     Discordserver:   https://discord.gg/wvcX92VyEH<p>
 *     Website:         https://vironlab.eu/ <p>
 *     Mail:            contact@vironlab.eu<p>
 *<p>
 */

package eu.vironlab.vextension.scoreboard.builder

import eu.vironlab.vextension.collection.DataPair
import eu.vironlab.vextension.factory.Factory
import eu.vironlab.vextension.scoreboard.ScoreboardUtil
import eu.vironlab.vextension.scoreboard.Sidebar
import eu.vironlab.vextension.scoreboard.SidebarLine
import eu.vironlab.vextension.scoreboard.bukkit.BukkitSidebar
import eu.vironlab.vextension.scoreboard.sponge.SpongeSidebar
import eu.vironlab.vextension.util.ServerType
import eu.vironlab.vextension.util.ServerUtil


class SidebarFactory : Factory<Sidebar> {

    val lines: MutableMap<String, SidebarLine> = mutableMapOf()
    var title: String = ""

    fun addLine(init: LineFactory.() -> Unit) {
        val builder: LineFactory = LineFactory()
        builder.init()
        val line = builder.create()
        this.lines[line.name] = line
    }

    fun addEmptyLine(name: String, score: Int) {
        addLine {
            this.score = score
            this.content = " "
            this.name = name
        }
    }

    override fun create(): Sidebar {
        val finalLines: MutableMap<String, DataPair<String, SidebarLine>> = mutableMapOf()
        val usedColors: MutableList<String> = mutableListOf()
        lines.forEach {
            val color = ScoreboardUtil.getAvailableColor(usedColors)
            usedColors.add(color)
            finalLines.put(it.key, DataPair(color, it.value))
        }
        return if (ServerUtil.SERVER_TYPE.equals(ServerType.BUKKIT)) {
            BukkitSidebar(finalLines, usedColors, title)
        } else {
            SpongeSidebar(finalLines, usedColors, title)
        }
    }

}

fun sidebar(init: SidebarFactory.() -> Unit): Sidebar {
    val builder: SidebarFactory = SidebarFactory()
    builder.init()
    return builder.create()
}