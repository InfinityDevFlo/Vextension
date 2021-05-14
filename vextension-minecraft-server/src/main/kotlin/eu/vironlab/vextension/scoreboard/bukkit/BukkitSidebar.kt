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

package eu.vironlab.vextension.scoreboard.bukkit

import eu.vironlab.vextension.bukkit.VextensionBukkit
import eu.vironlab.vextension.collection.DataPair
import eu.vironlab.vextension.concurrent.scheduleAsync
import eu.vironlab.vextension.multiversion.MinecraftVersion
import eu.vironlab.vextension.scoreboard.ScoreboardUtil
import eu.vironlab.vextension.scoreboard.Sidebar
import eu.vironlab.vextension.scoreboard.SidebarLine
import eu.vironlab.vextension.scoreboard.builder.SidebarLineImpl
import eu.vironlab.vextension.scoreboard.builder.sidebar
import eu.vironlab.vextension.util.ServerUtil
import java.util.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Team
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class BukkitSidebar(
    override val lines: MutableMap<String, DataPair<String, SidebarLine>>,
    val usedColors: MutableCollection<String>,
    override var title: String
) : Sidebar,
    Listener {

    val players: MutableList<Player> = mutableListOf()
    var listening: Boolean = false

    init {
        Bukkit.getPluginManager().registerEvents(this, VextensionBukkit.instance)
    }

    override fun addLine(line: SidebarLine) {
        if (!this.lines.containsKey(line.name)) {
            val color = ScoreboardUtil.getAvailableColor(usedColors)
            this.lines.put(line.name, DataPair(color, line))
            this.usedColors.add(color)
            scheduleAsync {
                this.players.forEach {
                    val scoreboard = it.scoreboard
                    val team: Team = scoreboard.registerNewTeam(line.name)
                    if (line.proceed != null) {
                        line.proceed!!.invoke(line, it.uniqueId)
                    }
                    val splittetLine = ScoreboardUtil.splitContent(line.content)
                    team.prefix = splittetLine.first
                    team.suffix = splittetLine.second
                    team.addEntry(color)
                    scoreboard.getObjective(DisplaySlot.SIDEBAR)!!.getScore(color).score = line.score
                }
            }
        }
    }

    override fun updateLine(name: String, line: SidebarLine) {
        scheduleAsync {
            lines[name] = DataPair(lines[name]!!.first, line)
            players.forEach {
                val scoreboard = it.scoreboard
                val team: Team? = scoreboard.getTeam(name)
                if (team != null) {
                    if (line.proceed != null) {
                        line.proceed!!.invoke(line, it.uniqueId)
                    }
                    val splittetLine = ScoreboardUtil.splitContent(line.content)
                    team.prefix = splittetLine.first
                    team.suffix = splittetLine.second
                    scoreboard.getObjective(DisplaySlot.SIDEBAR)!!.getScore(lines.get(name)!!.first).score = line.score
                }
            }
        }
    }

    override fun set(player: UUID) {
        val p: Player = Bukkit.getPlayer(player) ?: return
        val scoreboard = Bukkit.getScoreboardManager().newScoreboard
        if (scoreboard.getObjective(DisplaySlot.SIDEBAR) != null) {
            scoreboard.getObjective(DisplaySlot.SIDEBAR)!!.unregister()
        }
        if (scoreboard.getObjective("sidebar") != null) {
            scoreboard.getObjective("sidebar")!!.unregister()
        }
        val objective: Objective = scoreboard.registerNewObjective("sidebar", "dummy", this.title)
        objective.displaySlot = DisplaySlot.SIDEBAR
        this.lines.forEach { (name, pair) ->
            if (scoreboard.getTeam(name) != null) {
                scoreboard.getTeam(name)!!.unregister()
            }
            val team: Team = scoreboard.registerNewTeam(name)
            val line: SidebarLine = (pair.second as SidebarLineImpl).clone()
            if (line.proceed != null) {
                line.proceed!!.invoke(line, player)
            }
            //splittetLine = ScoreboardUtil.splitContent(pair.second.content)
            val splittetLine = ScoreboardUtil.splitContent(line.content)
            if (line.content.length > 32) {
                Executors.newSingleThreadScheduledExecutor().schedule({
                    for (index in pair.second.content.indices) {
                        team.suffix = pair.second.content.substring(index, index + 16)
                        Thread.sleep(1000)
                    }
                }, 3, TimeUnit.SECONDS)
            } else {
                team.prefix = splittetLine.first
                team.suffix = splittetLine.second
            }
            team.addEntry(pair.first)
            objective.getScore(pair.first).score = pair.second.score
        }
        p.scoreboard = scoreboard
        if (!players.contains(p))
            this.players.add(p)
    }

    override fun setAll() {
        Bukkit.getOnlinePlayers().forEach {
            set(it.uniqueId)
        }
    }

    override fun setAllAndListen() {
        setAll()
        this.listening = true
    }

    override fun cancelListening() {
        if (this.listening) {
            this.listening = false
        }
    }

    override fun removeAll() {
        scheduleAsync {
            players.forEach {
                remove(it.uniqueId)
            }
        }
    }

    override fun remove(player: UUID) {
        val p: Player = Bukkit.getPlayer(player) ?: return
        if (this.players.contains(p)) {
            p.scoreboard.getObjective(DisplaySlot.SIDEBAR)!!.unregister()
            p.scoreboard.clearSlot(DisplaySlot.SIDEBAR)
            this.players.remove(p)
        }
    }

    override fun updateTitle(title: String) {
        this.title = title
        scheduleAsync {
            players.forEach {
                it.scoreboard.getObjective(DisplaySlot.SIDEBAR)!!.displayName = title
            }
        }
    }

    @EventHandler
    fun handleJoin(event: PlayerJoinEvent) {
        if (listening) {
            set(event.player.uniqueId)
        }
    }

    @EventHandler
    fun handleQuit(event: PlayerQuitEvent) {
        if (this.players.contains(event.player)) {
            remove(event.player.uniqueId)
        }
    }
}
