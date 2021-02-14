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
    }

}

class DefaultVextension : Vextension {
    override lateinit var databaseClient: DatabaseClient
}

interface Vextension {

    var databaseClient: DatabaseClient

}