package eu.vironlab.vextension.discord

import eu.vironlab.vextension.database.Database
import eu.vironlab.vextension.database.DatabaseClient
import eu.vironlab.vextension.discord.embed.SimpleEmbedConfiguration
import eu.vironlab.vextension.discord.embed.toHex
import java.awt.Color
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.time.OffsetDateTime
import net.dv8tion.jda.api.EmbedBuilder


object DiscordUtil {

    //lateinit var userDatabase: Database

    lateinit var databaseClient: DatabaseClient

    @JvmStatic
    var EMBED_FOOTER = "Vextension - JVM Utility"

    @JvmStatic
    var noPermsMessage: SimpleEmbedConfiguration =
        SimpleEmbedConfiguration("No Permissions", "You dont have the Permission to do that", Color.RED.toHex())

    @JvmStatic
    var onlyGuild: SimpleEmbedConfiguration =
        SimpleEmbedConfiguration("Wrong Channel", "You can use this command only on Guilds", Color.RED.toHex())

    @JvmStatic
    var onlyDirectMessage: SimpleEmbedConfiguration =
        SimpleEmbedConfiguration("Wrong Channel", "You can use this command only on Private Message", Color.RED.toHex())

    @JvmStatic
    fun defaultBuilder(): EmbedBuilder =
        EmbedBuilder().setFooter(DiscordUtil.EMBED_FOOTER).setColor(Color.CYAN).setTimestamp(
            OffsetDateTime.now()
        )

    /**
     * Download the Latest JDA File
     */
    @JvmStatic
    fun loadJDA() {
        /*val jdaDirectDownload =
            "https://github.com/DV8FromTheWorld/JDA/releases/download/v4.2.0/JDA-4.2.0_168-withDependencies-min.jar"
        try {
            val dest: File = File(OldDependencyLoader.dataPath, "JDA-with-dependencies.jar")
            if (!dest.exists()) {
                dest.parentFile.mkdirs()
                val requestURL = URL(jdaDirectDownload)
                Files.copy(requestURL.openStream(), dest.toPath())
            }
            try {
                DependencyClassloader().addJarToClasspath(dest)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }*/
    }

}