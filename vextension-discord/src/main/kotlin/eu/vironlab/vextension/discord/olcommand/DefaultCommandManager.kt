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

package eu.vironlab.vextension.discord.olcommand

import eu.vironlab.vextension.discord.embed.SimpleEmbedConfiguration
import eu.vironlab.vextension.discord.member.toVextension
import eu.vironlab.vextension.document.DocumentManagement
import java.awt.Color
import java.util.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter


class DefaultCommandManager(val jda: JDA, val prefix: String) : CommandManager, ListenerAdapter() {

    override val commands: MutableMap<String, Command> = mutableMapOf()
    val aliases: MutableMap<String, String> = mutableMapOf()
    override var noPrivateChannelMessage: SimpleEmbedConfiguration = SimpleEmbedConfiguration(
        "No Private Channel Supported",
        "This command is not for Private Channels. Please use this command on a Guild",
        Color.RED
    )
    override var commandNotFoundMessage: SimpleEmbedConfiguration =
        SimpleEmbedConfiguration("Command not Found", "The Command %cmd% could not be found", Color.RED)


    init {
        this.jda.addEventListener(this)
    }

    override fun registerCommand(
        command: String,
        executor: CommandExecutor,
        description: String,
        target: CommandTarget,
        vararg aliases: String
    ) {
        if (!this.commands.containsKey(command)) {
            this.commands.put(command, Command(command, description, executor, aliases))
            for (alias in aliases) {
                this.aliases.put(alias, command)
            }
        } else {
            throw IllegalStateException("Cannot register Command twice")
        }
    }

    override fun unregister(command: String) {
        this.commands.remove(command)
        this.aliases.forEach { alias, cmd ->
            if (cmd == command) {
                this.aliases.remove(alias)
            }
        }
    }

    enum class CommandTarget {
        PRIVATE, PUBLIC
    }


    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val message: String = event.message.contentDisplay
        if (message.startsWith(prefix)) {
            val args = message.split(" ")
            if (args.size > 1) {
                val cmdName = args[0]
                var cmd: Command? = null
                if (commands.containsKey(cmd)) {
                    cmd = commands.get(cmdName)
                } else if (aliases.containsKey(cmdName)) {
                    cmd = commands.get(aliases.get(cmdName))
                }
                if (cmd != null) {
                    cmd.executor.execute(
                        event.member!!.toVextension(),
                        event.message,
                        Arrays.asList(args).removeFirst().toTypedArray(),
                        event.channel,
                        event
                    )
                } else {
                    event.channel.sendMessage(
                        this.commandNotFoundMessage.toEmbed(
                            event.author.avatarUrl!!,
                            DocumentManagement.newDocument("message", "cmd", cmdName)
                        )
                    )
                }
            }
        }
    }

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        event.channel.sendMessage(this.noPrivateChannelMessage.toEmbed(this.jda.selfUser.avatarUrl!!))
    }

}