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

package eu.vironlab.vextension.scoreboard.sponge

import eu.vironlab.vextension.collection.DataPair
import eu.vironlab.vextension.scoreboard.ScoreboardUtil
import eu.vironlab.vextension.scoreboard.Sidebar
import eu.vironlab.vextension.scoreboard.SidebarLine
import eu.vironlab.vextension.sponge.VextensionSponge
import java.util.*
import java.util.concurrent.CompletableFuture
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.scoreboard.Team
import org.spongepowered.api.scoreboard.critieria.Criteria
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots
import org.spongepowered.api.scoreboard.objective.Objective
import org.spongepowered.api.text.Text


class SpongeSidebar(
    override val lines: MutableMap<String, DataPair<String, SidebarLine>>,
    val usedColors: MutableCollection<String>,
    override var title: String
) : Sidebar {

    val players: MutableList<Player> = mutableListOf()
    var listening: Boolean = false
    val objectiveBuilder: Objective.Builder = Sponge.getRegistry().createBuilder(Objective.Builder::class.java)
    val teamBuilder: Team.Builder = Sponge.getRegistry().createBuilder(Team.Builder::class.java)

    init {
        Sponge.getEventManager().registerListeners(this, VextensionSponge.instance)
    }

    override fun addLine(line: SidebarLine) {
        if (!this.lines.containsKey(line.name)) {
            val color = ScoreboardUtil.getAvailableColor(usedColors)
            this.lines.put(line.name, DataPair(color, line))
            this.usedColors.add(color)
            CompletableFuture.supplyAsync {
                this.players.forEach {
                    val scoreboard = it.scoreboard
                    val team: Team = teamBuilder.name(line.name).build()
                    if (line.proceed != null) {
                        line.proceed!!.invoke(line, it.uniqueId)
                    }
                    val splittetLine = ScoreboardUtil.splitContent(line.content)
                    team.prefix = Text.of(splittetLine.first)
                    team.suffix = Text.of(splittetLine.second)
                    team.addMember(Text.of(color))
                    scoreboard.getObjective(DisplaySlots.SIDEBAR).get().getOrCreateScore(Text.of(color)).score =
                        line.score
                }
            }
        }
    }

    override fun updateLine(name: String, line: SidebarLine) {
        CompletableFuture.supplyAsync {
            players.forEach {
                val scoreboard = it.scoreboard
                val optionalTeam: Optional<Team> = scoreboard.getTeam(name)
                if (optionalTeam.isPresent) {
                    val team = optionalTeam.get()
                    if (line.proceed != null) {
                        line.proceed!!.invoke(line, it.uniqueId)
                    }
                    val splittetLine = ScoreboardUtil.splitContent(line.content)
                    team.prefix = Text.of(splittetLine.first)
                    team.suffix = Text.of(splittetLine.second)
                    scoreboard.getObjective(DisplaySlots.SIDEBAR).get().getScore(Text.of(lines.get(name)!!.first))
                        .get().score = line.score
                }
            }
        }
    }

    override fun set(player: UUID) {
        CompletableFuture.supplyAsync {
            val optionalPlayer: Optional<Player> = Sponge.getServer().getPlayer(player)
            if (optionalPlayer.isPresent) {
                val p = optionalPlayer.get()
                val scoreboard = p.scoreboard
                scoreboard.getObjective(DisplaySlots.SIDEBAR).ifPresent {
                    scoreboard.removeObjective(it)
                }
                scoreboard.getObjective("sidebar").ifPresent {
                    scoreboard.removeObjective(it)
                }
                val objective: Objective =
                    objectiveBuilder.criterion(Criteria.DUMMY).name("sidebar").displayName(Text.of(this.title)).build()
                this.lines.forEach { name, pair ->
                    scoreboard.getTeam(name).ifPresent {
                        it.unregister()
                    }
                    val team: Team = teamBuilder.name(name).build()
                    if (pair.second.proceed != null) {
                        pair.second.proceed!!.invoke(pair.second, player)
                    }
                    val splittetLine = ScoreboardUtil.splitContent(pair.second.content)
                    team.prefix = Text.of(splittetLine.first)
                    team.suffix = Text.of(splittetLine.second)
                    team.addMember(Text.of(pair.first))
                    objective.getOrCreateScore(Text.of(pair.first)).score = pair.second.score
                }
                scoreboard.addObjective(objective)
                scoreboard.updateDisplaySlot(objective, DisplaySlots.SIDEBAR)
                this.players.add(p)
            }
        }
    }

    override fun setAll() {
        Sponge.getServer().onlinePlayers.forEach {
            set(it.uniqueId)
        }
    }

    override fun setAllAndListen() {
        Sponge.getServer().onlinePlayers.forEach {
            set(it.uniqueId)
        }
        this.listening = true
    }

    override fun cancelListening() {
        if (this.listening) {
            this.listening = false
        }
    }

    override fun removeAll() {
        CompletableFuture.supplyAsync {
            players.forEach {
                remove(it.uniqueId)
            }
        }
    }

    override fun remove(player: UUID) {
        Sponge.getServer().getPlayer(player).ifPresent {
            if (this.players.contains(player)) {
                it.scoreboard.removeObjective(it.scoreboard.getObjective(DisplaySlots.SIDEBAR).get())
                it.scoreboard.clearSlot(DisplaySlots.SIDEBAR)
                this.players.remove(player)
            }
        }
    }

    override fun updateTitle(title: String) {
        this.title = title
        CompletableFuture.supplyAsync {
            players.forEach {
                it.scoreboard.getObjective(DisplaySlots.SIDEBAR).get().displayName = Text.of(title)
            }
        }
    }

    @Listener
    fun handleJoin(event: ClientConnectionEvent.Join) {
        if (listening) {
            set(event.targetEntity.uniqueId)
        }
    }

    @Listener
    fun handleQuit(event: ClientConnectionEvent.Disconnect) {
        if (this.players.contains(event.targetEntity.uniqueId)) {
            remove(event.targetEntity.uniqueId)
        }
    }
}