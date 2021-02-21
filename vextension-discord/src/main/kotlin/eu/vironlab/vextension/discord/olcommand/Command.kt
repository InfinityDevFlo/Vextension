package eu.vironlab.vextension.discord.olcommand

data class Command(val name: String, val description: String, val executor: CommandExecutor, val aliases: Array<out String> = arrayOf())
