package eu.vironlab.vextension

import eu.vironlab.vextension.database.DatabaseClient

object VextensionAPI {

    @JvmStatic
    lateinit var instance: Vextension

    @JvmStatic
    fun initialize() {
        instance = DefaultVextension()
    }

}

class DatabaseConnectionData(val database: String) {
    fun toSql() = "none"
    fun toMongo() = "none"
}

class DefaultVextension : Vextension {
    private var databaseClient: DatabaseClient? = null

    override fun getDatabaseClient(): DatabaseClient {
        return this.databaseClient!! //?: throw ClientNotInitializedException("You have to init the client first")
    }

}

interface Vextension {

    fun getDatabaseClient(): DatabaseClient

}