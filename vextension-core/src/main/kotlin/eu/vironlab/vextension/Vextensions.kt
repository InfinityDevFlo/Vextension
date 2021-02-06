package eu.vironlab.vextension

import eu.vironlab.vextension.database.ClientNotInitializedException
import eu.vironlab.vextension.database.DatabaseClient
import eu.vironlab.vextension.database.DatabaseClientType

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
        return this.databaseClient ?: throw ClientNotInitializedException("You have to init the client first")
    }


    override fun initDatabase(type: DatabaseClientType, connectionData: DatabaseConnectionData) {
        TODO("Not yet implemented")
    }
}

interface Vextension {

    fun getDatabaseClient(): DatabaseClient

    fun initDatabase(type: DatabaseClientType, connectionData: DatabaseConnectionData)

}