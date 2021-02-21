package eu.vironlab.vextension.discord.command.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class SubCommand(val command: String, val name: String)
