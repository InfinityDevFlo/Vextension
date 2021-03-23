package eu.vironlab.vextension

import eu.vironlab.vextension.database.DatabaseClient
import java.io.File

object VextensionAPI {

    @JvmStatic
    lateinit var instance: Vextension

    @JvmStatic
    fun initialize(
        vextension: Vextension,
        depenedencyDir: File = File(".libs"),
    ) {
        OldDependencyLoader.dataPath = depenedencyDir
        instance = vextension
    }

}


interface Vextension {

    var databaseClient: DatabaseClient

}