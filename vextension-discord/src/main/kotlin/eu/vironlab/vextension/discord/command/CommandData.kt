package eu.vironlab.vextension.discord.command

import eu.vironlab.vextension.discord.command.executor.CommandExecutor

data class CommandData(val name: String, val description: String, val aliases: Array<String>, val target: CommandChannelTarget, val executor: CommandExecutor)
