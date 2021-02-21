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

package eu.vironlab.vextension.discord.command.commands

import eu.vironlab.vextension.discord.DiscordUtil
import eu.vironlab.vextension.discord.command.CommandChannelTarget
import eu.vironlab.vextension.discord.command.CommandManager
import eu.vironlab.vextension.discord.command.CommandSource
import eu.vironlab.vextension.discord.command.annotation.Command
import eu.vironlab.vextension.discord.command.executor.CommandExecutor
import eu.vironlab.vextension.discord.command.executor.SubCommandExecutor
import java.awt.Color
import java.lang.StringBuilder
import java.time.OffsetDateTime
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageChannel

@Command("help", CommandChannelTarget.ALL, "Gives Help", arrayOf("?", "hilfe"))
class HelpCommand(val commandManager: CommandManager) : CommandExecutor {
    override var subCommands: MutableMap<String, SubCommandExecutor> = mutableMapOf()

    override fun execute(
        source: CommandSource,
        channel: MessageChannel,
        args: Array<String>,
        isGuild: Boolean,
        guild: Guild?,
        jda: JDA
    ) {
        val builder: EmbedBuilder = EmbedBuilder()
        builder.setThumbnail(jda.selfUser.avatarUrl)
        builder.setFooter(DiscordUtil.EMBED_FOOTER)
        builder.setColor(Color.CYAN)
        builder.setDescription("Help for ${jda.selfUser.name}")
        builder.setTimestamp(OffsetDateTime.now())
        builder.setAuthor(jda.selfUser.name)
        commandManager.commands.forEach { name, command ->
            val aliases: StringBuilder = StringBuilder()
            if (!command.aliases.isEmpty()) {
                command.aliases.forEach {
                    aliases.append("${it} ,")
                }
            }
            if (isGuild) {
                if (command.target != CommandChannelTarget.PRIVATE) {
                    builder.addField(
                        "${commandManager.prefix}${name}, ${
                            aliases.toString().substring(0, aliases.toString().length - 2)
                        }", command.description, false
                    )
                }
            } else {
                if (command.target != CommandChannelTarget.GUILD) {
                    builder.addField(
                        "${commandManager.prefix}${name}, ${
                            aliases.toString().substring(0, aliases.toString().length - 2)
                        }", command.description, false
                    )
                }
            }
        }
        channel.sendMessage(builder.build()).queue()
    }

}
