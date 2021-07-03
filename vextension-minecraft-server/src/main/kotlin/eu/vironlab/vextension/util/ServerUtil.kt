package eu.vironlab.vextension.util

import eu.vironlab.vextension.multiversion.MinecraftVersion
import org.bukkit.Bukkit


object ServerUtil {

    /**
     * Get the Type of a Server
     */
    @JvmStatic
    val SERVER_TYPE: ServerType = run {
        return@run try {
            Class.forName("org.bukkit.Bukkit", false, this::class.java.classLoader)
            ServerType.BUKKIT
        } catch (e: Exception) {
            try {
                Class.forName("org.spongepowered.api.Sponge", false, this::class.java.classLoader)
                ServerType.SPONGE
            } catch (e: Exception) {
                throw UnsupportedServerTypeException("Please use one of the Following Types of Servers: Bukkit")
            }
        }
    }


    @JvmStatic
    val NMS_PACKAGE_NAME: String = "net.minecraft.server" + if (SERVER_TYPE == ServerType.BUKKIT) {
        Bukkit.getServer().javaClass.getPackage().name.let { it.substring(it.lastIndexOf(".") + 1, it.length) }
    } else {
        //Sponge.getServer().javaClass.getPackage().name.let { it.substring(it.lastIndexOf(".") + 1, it.length) }
    }


    /**
     * Returns the MinecraftVersion of the Server using NMS
     * @return The Server Version or if invalid UNKNOWN
     */
    @JvmStatic
    fun getMinecraftVersion(): MinecraftVersion {
        MinecraftVersion.values().forEach {
            if (it == MinecraftVersion.UNKNOWN) return@forEach
            try {
                if (Package.getPackage("net.minecraft.server.$it") != null) return@getMinecraftVersion it
            } catch (_: Exception) {
            }
        }
        return MinecraftVersion.UNKNOWN
    }
}

class UnsupportedServerTypeException(msg: String) : Exception(msg)

enum class ServerType {
    BUKKIT, SPONGE
}