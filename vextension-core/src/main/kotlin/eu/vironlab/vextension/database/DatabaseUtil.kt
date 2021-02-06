package eu.vironlab.vextension.database

import eu.vironlab.vextension.database.DatabaseUtil.getUnignoredFields
import java.io.File
import java.lang.reflect.Field
import java.util.*

object DatabaseUtil {

    @JvmStatic
    fun <T> getUnignoredFields(clazz: Class<T>): Collection<Field> {
        val raw: List<Field> = clazz.declaredFields.toList()
        return raw.filter {
            print(!it.isAnnotationPresent(Ignored::class.java))
            return@filter !it.isAnnotationPresent(Ignored::class.java)
        }
    }

}
enum class DatabaseClientType {
    MONGO, SQL
}

class ConnectionStringBuilder() {

    fun file(file: File): String {
        TODO("Not yet implemented")
    }

    fun remoteMongo(host: String, port: Int, user: String, password: String, database: String): String {
        return "mongodb://${user}:${password}@${host}:${port}/${database}"
    }

    fun remoteSql(host: String, port: Int, user: String, password: String, database: String): String {
        return "jdbc:mysql://${host}:${port}/${database}?user=${user}&password=${password}"
    }
}
