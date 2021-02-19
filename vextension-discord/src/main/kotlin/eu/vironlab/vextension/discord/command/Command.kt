package eu.vironlab.vextension.discord.command

data class Command(val name: String, val description: String, val executor: CommandExecutor, val aliases: Array<out String> = arrayOf())
