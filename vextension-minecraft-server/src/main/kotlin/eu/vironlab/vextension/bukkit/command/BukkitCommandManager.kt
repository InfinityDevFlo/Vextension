/**
 * Copyright © 2020 | vironlab.eu | Licensed under the GNU General Public license Version 3<p>
 * <p>
 * ___    _______                        ______         ______  <p>
 * __ |  / /___(_)______________ _______ ___  / ______ ____  /_ <p>
 * __ | / / __  / __  ___/_  __ \__  __ \__  /  _  __ `/__  __ \<p>
 * __ |/ /  _  /  _  /    / /_/ /_  / / /_  /___/ /_/ / _  /_/ /<p>
 * _____/   /_/   /_/     \____/ /_/ /_/ /_____/\__,_/  /_.___/ <p>
 * <p>
 * ____  _______     _______ _     ___  ____  __  __ _____ _   _ _____ <p>
 * |  _ \| ____\ \   / / ____| |   / _ \|  _ \|  \/  | ____| \ | |_   _|<p>
 * | | | |  _|  \ \ / /|  _| | |  | | | | |_) | |\/| |  _| |  \| | | |  <p>
 * | |_| | |___  \ V / | |___| |__| |_| |  __/| |  | | |___| |\  | | |  <p>
 * |____/|_____|  \_/  |_____|_____\___/|_|   |_|  |_|_____|_| \_| |_|  <p>
 * <p>
 * <p>
 * This program is free software: you can redistribute it and/or modify<p>
 * it under the terms of the GNU General Public License as published by<p>
 * the Free Software Foundation, either version 3 of the License, or<p>
 * (at your option) any later version.<p>
 * <p>
 * This program is distributed in the hope that it will be useful,<p>
 * but WITHOUT ANY WARRANTY; without even the implied warranty of<p>
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<p>
 * GNU General Public License for more details.<p>
 * <p>
 * You should have received a copy of the GNU General Public License<p>
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.<p>
 *<p>
 *   Creation: Sonntag 11 Juli 2021 17:12:42<p>
 *<p>
 * <p>
 * Contact:<p>
 * <p>
 * Discordserver:   https://discord.gg/wvcX92VyEH<p>
 * Website:         https://vironlab.eu/ <p>
 * Mail:            contact@vironlab.eu<p>
 * <p>
 */

package eu.vironlab.vextension.bukkit.command;

import eu.vironlab.vextension.command.AbstractCommandManager
import eu.vironlab.vextension.command.executor.CommandExecutor
import eu.vironlab.vextension.extension.toCleanString
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.command.Command as BukkitCommand

class BukkitCommandManager(val plugin: JavaPlugin, val prefix: String = plugin.name) :
    AbstractCommandManager<BukkitCommandSource, BukkitCommandContext>(BukkitCommandContext::class.java) {

    override fun register(cmd: CommandExecutor<BukkitCommandSource, BukkitCommandContext>): Boolean {
        if (super.register(cmd)) {
            val data = this.commands.values.last()
            Bukkit.getCommandMap().register(prefix, Command(data.name, data.aliases.toList()))
            return true
        }
        return false
    }

    override fun close() {
        this.aliases.forEach { (name, data) ->
            Bukkit.getCommandMap().knownCommands.remove(name)
        }
    }

    inner class Command(name: String, aliases: List<String>) : BukkitCommand(name) {

        init {
            super.setAliases(aliases)
        }

        override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
            return this@BukkitCommandManager.parseLine(
                "$commandLabel ${args.toCleanString(true)}",
                BukkitCommandSource(sender, sender.name)
            )
        }
    }

}