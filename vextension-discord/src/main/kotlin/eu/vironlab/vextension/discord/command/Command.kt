package eu.vironlab.vextension.discord.command

data class Command(val name: String, val description: String, val aliases: Array<String>, val target: CommandChannelTarget)
