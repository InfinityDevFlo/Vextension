package eu.vironlab.vextension.discord.command.annotation

import eu.vironlab.vextension.discord.command.CommandChannelTarget

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Command(
    val name: String,
    val target: CommandChannelTarget,
    val description: String,
    val aliases: Array<String>
)
