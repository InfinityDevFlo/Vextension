package eu.vironlab.vextension.util

import eu.vironlab.vextension.multiversion.MinecraftVersion


object ServerUtil {

    /**
     * Get the Type of a Server
     */
    @JvmStatic
    fun getServerType(): ServerType {
        return try {
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
    fun getMinecraftVersion(): MinecraftVersion {
        var version: MinecraftVersion? = null
        MinecraftVersion.values().forEach {
            if (version != null) return@forEach
            try {
                if (Package.getPackage("net.minecraft.server.$it") != null) return@forEach
                version = it
            } catch (_: Exception) {
            }
        }
        return version ?: MinecraftVersion.UNKNOWN
    }
}

class UnsupportedServerTypeException(msg: String) : Exception(msg)

enum class ServerType {
    BUKKIT, SPONGE
}