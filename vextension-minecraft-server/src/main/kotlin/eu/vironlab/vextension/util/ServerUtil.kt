package eu.vironlab.vextension.util


object ServerUtil {


    @JvmStatic
    fun getServerType(): ServerType {
        return try {
            Class.forName("org.bukkit.Bukkit", false, this::class.java.classLoader)
            ServerType.BUKKIT
        }catch (e: Exception) {
            try {
                Class.forName("org.spongepowered.api.Sponge", false, this::class.java.classLoader)
                ServerType.SPONGE
            } catch (e: Exception) {
                throw UnsupportedServerTypeException("Please use one of the Following Types of Servers: Bukkit")
            }
        }
    }

}

class UnsupportedServerTypeException(msg: String): Exception(msg)

enum class ServerType {
    BUKKIT, SPONGE
}