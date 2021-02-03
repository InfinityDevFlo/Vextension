package eu.vironlab.vextension

import eu.vironlab.vextension.database.ClientNotInitializedException
import eu.vironlab.vextension.database.DatabaseClient
import eu.vironlab.vextension.database.DatabaseClientType
import eu.vironlab.vextension.database.DatabaseConnectionData
import eu.vironlab.vextension.database.mongodb.MongoDatabaseClient
import eu.vironlab.vextension.database.sql.SqlDatabaseClient
import eu.vironlab.vextension.vironlab.VironLabAPI

object VextensionAPI {

    @JvmStatic
    lateinit var instance: Vextension

    @JvmStatic
    fun initialize() {
        instance = DefaultVextension()
    }

}

class DefaultVextension : Vextension {
    private var databaseClient: DatabaseClient? = null

    override fun getDatabaseClient(): DatabaseClient {
        return this.databaseClient ?: throw ClientNotInitializedException("You have to init the client first")
    }

    override fun getVironLabAPI(): VironLabAPI {
        TODO("Not yet implemented")
    }


    override fun initDatabase(type: DatabaseClientType, connectionData: DatabaseConnectionData) {
        when(type) {
            DatabaseClientType.SQL -> {
                this.databaseClient = SqlDatabaseClient(connectionData.toSql())
            }
            DatabaseClientType.MONGO -> {
                this.databaseClient = MongoDatabaseClient(connectionData.database, connectionData.toMongo())
            }
        }
    }
}

interface Vextension {

    fun getDatabaseClient(): DatabaseClient

    fun getVironLabAPI(): VironLabAPI

    fun initDatabase(type: DatabaseClientType, connectionData: DatabaseConnectionData)

}