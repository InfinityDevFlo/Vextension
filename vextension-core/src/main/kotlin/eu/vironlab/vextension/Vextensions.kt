package eu.vironlab.vextension

import eu.vironlab.vextension.database.DatabaseClient
import eu.vironlab.vextension.dependency.DependencyLoader
import java.io.File

object VextensionAPI {

    @JvmStatic
    lateinit var instance: Vextension

    @JvmStatic
    fun initialize(vextension: Vextension = DefaultVextension(), depenedencyDir: File = File(".libs")) {
        instance = vextension
        DependencyLoader.dataPath = depenedencyDir
        DependencyLoader.requireUnsafe("com.google.code.gson:gson:2.8.6")
        DependencyLoader.requireUnsafe("org.yaml:snakeyaml:1.27")
        DependencyLoader.requireUnsafe("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.1")
        DependencyLoader.requireUnsafe("com.fasterxml.jackson.core:jackson-databind:2.12.1")
        DependencyLoader.requireUnsafe("com.fasterxml.jackson.core:jackson-core:2.12.1")
        DependencyLoader.requireUnsafe("com.fasterxml.jackson.core:jackson-annotations:2.12.1")
        DependencyLoader.requireUnsafe("org.codehaus.woodstox:stax2-api:4.2.1")
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