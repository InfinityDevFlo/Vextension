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

package eu.vironlab.vextension.discord.command

import eu.vironlab.vextension.discord.DiscordUtil
import eu.vironlab.vextension.discord.command.annotation.Command
import eu.vironlab.vextension.discord.command.executor.CommandExecutor
import eu.vironlab.vextension.discord.embed.SimpleEmbedConfiguration
import eu.vironlab.vextension.discord.embed.toHex
import eu.vironlab.vextension.discord.extension.toVextension
import eu.vironlab.vextension.document.DocumentManagement
import java.awt.Color
import java.util.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class DefaultCommandManager(override val prefix: String, val jda: JDA) : CommandManager, ListenerAdapter() {
    override val commands: MutableMap<String, CommandData> = mutableMapOf()
    val aliases: MutableMap<String, String> = mutableMapOf()
    override var commandNotFoundMessage: SimpleEmbedConfiguration =
        SimpleEmbedConfiguration("Command not Found", "The Command %cmd% could not be found", Color.RED.toHex())

    init {
        jda.addEventListener(this)
        println("Initialized CommandManager")
    }

    override fun register(commandExecutor: CommandExecutor) {
        if (!commandExecutor::class.java.isAnnotationPresent(Command::class.java)) {
            throw UnsupportedOperationException("Cannot register command without ${Command::class.qualifiedName} Annotation")
        }
        val data = commandExecutor::class.java.getAnnotation(Command::class.java)
        data.aliases.forEach {
            this.aliases.put(data.name, it)
        }
        this.commands.put(
            data.name,
            CommandData(data.name, data.description, data.aliases, data.target, commandExecutor)
        )
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val message: String = event.message.contentRaw
        if (message.startsWith(prefix)) {
            val args = message.split(" ")
            val cmdName = args[0].substring(1)
            var cmd: CommandData? = null
            if (commands.containsKey(cmdName)) {
                cmd = commands.get(cmdName)
            } else if (aliases.containsKey(cmdName)) {
                cmd = commands.get(aliases.get(cmdName))
            }
            if (cmd != null) {
                if (event.isFromGuild) {
                    if (cmd.target == CommandChannelTarget.PRIVATE) {
                        event.channel.sendMessage(DiscordUtil.onlyDirectMessage.toEmbed())
                        println("${event.author.name} tried to use a Public command in Private Chat")
                        return
                    }
                } else {
                    if (cmd.target == CommandChannelTarget.GUILD) {
                        event.channel.sendMessage(DiscordUtil.onlyGuild.toEmbed())
                        println("${event.author.name} tried to use a Public command in Private Chat")
                        return
                    }
                }
                if (args.size >= 2) {
                    val finalArgs = args.drop(1)
                    if (finalArgs.size > 1) {
                        if (cmd.executor.subCommands.containsKey(finalArgs[0])) {
                            cmd.executor.subCommands.get(finalArgs[0])!!.execute(
                                cmd.name,
                                event.author.toVextension(),
                                event.channel,
                                event.message,
                                finalArgs.drop(1).toTypedArray(),
                                event.isFromGuild,
                                event.guild,
                                this.jda
                            )
                            println("Subcommand > ${finalArgs[0]} from Command ${cmd.name} executed by ${event.author.name} [ ${event.author.id} ]")
                            return
                        }
                    }
                }
                    cmd.executor.execute(
                        event.author.toVextension(),
                        event.channel,
                        event.message,
                        args.toTypedArray(),
                        event.isFromGuild,
                        event.guild,
                        this.jda
                    )
                println("Command > ${cmd.name} executed by ${event.author.name} [ ${event.author.id} ]")
                return
            } else {
                println("${event.author.name} tried to use unknown Command: $cmdName")
                event.channel.sendMessage(
                    this.commandNotFoundMessage.toEmbed(
                        event.author.avatarUrl!!,
                        DocumentManagement.newDocument("message", "cmd", cmdName)
                    )
                ).queue()
            }
        }
    }


}