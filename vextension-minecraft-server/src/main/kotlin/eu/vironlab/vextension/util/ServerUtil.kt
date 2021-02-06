package eu.vironlab.vextension.util


object ServerUtil {


    @JvmStatic
    fun getServerType(): ServerType {
        try {
            Class.forName("org.bukkit.Bukkit")
            return ServerType.BUKKIT
        }catch (e: Exception) {
            throw UnsupportedServerTypeException("Please use one of the Following Types of Servers: Bukkit")
        }
    }

}

class UnsupportedServerTypeException(msg: String): Exception(msg)

enum class ServerType {
    BUKKIT
}