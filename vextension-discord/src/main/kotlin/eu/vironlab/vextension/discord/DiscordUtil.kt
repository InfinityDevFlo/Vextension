package eu.vironlab.vextension.discord

import eu.vironlab.vextension.VextensionAPI
import eu.vironlab.vextension.database.Database
import eu.vironlab.vextension.dependency.DependencyClassloader
import eu.vironlab.vextension.dependency.DependencyLoader
import eu.vironlab.vextension.document.DefaultDocument
import eu.vironlab.vextension.document.Document
import java.io.File
import java.net.URL
import java.nio.file.Files


object DiscordUtil {

    @JvmStatic
    var userDatabase: Database<DefaultDocument, String> = VextensionAPI.instance.databaseClient.getBasicDatabase("discord_users")

    /**
     * Download the Latest JDA File
     */
    @JvmStatic
    fun loadJDA() {
        val jdaDirectDownload =
            "https://github.com/DV8FromTheWorld/JDA/releases/download/v4.2.0/JDA-4.2.0_228-withDependencies-min.jar"
        try {
            val dest: File = File(DependencyLoader.dataPath, "JDA-with-dependencies.jar")
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
        }
    }

}