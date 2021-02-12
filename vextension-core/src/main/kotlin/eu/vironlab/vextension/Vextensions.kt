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
        DependencyLoader.require("com.google.code.gson:gson:2.8.6")
        DependencyLoader.require("org.yaml:snakeyaml:1.27")
        DependencyLoader.require("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.1")
        DependencyLoader.require("com.fasterxml.jackson.core:jackson-databind:2.12.1")
        DependencyLoader.require("com.fasterxml.jackson.core:jackson-core:2.12.1")
        DependencyLoader.require("com.fasterxml.jackson.core:jackson-annotations:2.12.1")
        DependencyLoader.require("org.codehaus.woodstox:stax2-api:4.2.1")
    }

}

class DefaultVextension : Vextension {
    override lateinit var databaseClient: DatabaseClient
}

interface Vextension {

    var databaseClient: DatabaseClient

}