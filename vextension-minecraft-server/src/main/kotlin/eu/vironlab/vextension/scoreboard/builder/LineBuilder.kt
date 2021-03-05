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

import eu.vironlab.vextension.builder.Builder
import eu.vironlab.vextension.scoreboard.SidebarLine
import java.util.*
import org.bukkit.Bukkit


class LineBuilder : Builder<SidebarLine> {

    var name: String = ""
    var content: String = ""
    var score: Int = 1
    var proceed: ((SidebarLine, UUID) -> Unit?)? = null

    fun proceed(proceed: (SidebarLine, UUID) -> Unit) {
        this.proceed = proceed
    }


    override fun build(): SidebarLine {
        return SidebarLineImpl(name, content, score, proceed)
    }
}

fun buildLine(init: LineBuilder.() -> Unit): SidebarLine {
    val builder: LineBuilder = LineBuilder()
    builder.init()
    return builder.build()
}

@FunctionalInterface
interface LineConsumer<K, V> {

    fun accept(line: K, player: V): String

}

class SidebarLineImpl(
    override val name: String, override var content: String, override var score: Int,
    override var proceed: ((SidebarLine, UUID) -> Unit?)?
) : SidebarLine {
    fun clone(): SidebarLineImpl {
        return SidebarLineImpl(name, content, score, proceed)
    }
}