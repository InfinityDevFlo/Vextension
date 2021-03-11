````kotlin
//Classes you need to Import for this example

import eu.vironlab.vextension.discord.command.CommandChannelTarget
import eu.vironlab.vextension.discord.command.CommandManager
import eu.vironlab.vextension.discord.command.CommandSource
import eu.vironlab.vextension.discord.command.DefaultCommandManager
import eu.vironlab.vextension.discord.command.annotation.Command
import eu.vironlab.vextension.discord.command.executor.CommandExecutor
import eu.vironlab.vextension.discord.command.executor.SubCommandExecutor
import java.lang.StringBuilder
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.requests.GatewayIntent

//Class wich can be launched by "main" Method
class Bot : DiscordBot(loadJda = true) {
    //Creating the JDA
    override var jda: JDA =
        JDABuilder.createDefault(this.token).enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)).build()
    //Init the Command System with out Default Implementation
    override var commandManager: CommandManager = DefaultCommandManager("!", this.jda)

    init {
        //Wait until the JDA is ready
        jda.awaitReady()
        //Register a Command
        commandManager.register(ExampleCommand())
    }
}

//Command Example
@Command("test", CommandChannelTarget.GUILD, "TestCommand", arrayOf())
class ExampleCommand : CommandExecutor {
    override val subCommands: MutableMap<String, SubCommandExecutor> = mutableMapOf()

    init {
        subCommands.put("printProps", ExampleSubCommand())
    }

    override fun execute(
        source: CommandSource,
        channel: MessageChannel,
        message: Message,
        args: Array<String>,
        isGuild: Boolean,
        guild: Guild?,
        jda: JDA
    ) {
        val builder: EmbedBuilder = DiscordUtil.defaultBuilder().setAuthor(source.name).setThumbnail(source.avatarUrl)
            .setTitle("Message from ${source.name}")
        val descriptionBuilder: StringBuilder = StringBuilder()
        for (arg in args) {
            descriptionBuilder.append(arg)
        }
        builder.setDescription(descriptionBuilder.toString())
        channel.sendMessage(builder.build()).queue()
    }

}

//Subcommand wich will be executet when the message is "!test printProps"
class ExampleSubCommand : SubCommandExecutor {
    override fun execute(
        command: String,
        source: CommandSource,
        channel: MessageChannel,
        message: Message,
        args: Array<String>,
        isGuild: Boolean,
        guild: Guild?,
        jda: JDA
    ) {
        println(source.properties.toJson())
    }
}
````