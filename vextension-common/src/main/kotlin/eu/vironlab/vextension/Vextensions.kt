package eu.vironlab.vextension

import eu.vironlab.vextension.database.DatabaseClient
import eu.vironlab.vextension.database.RemoteConnectionData
import eu.vironlab.vextension.database.mongo.MongoDatabaseClient
import eu.vironlab.vextension.dependency.DependencyLoader
import eu.vironlab.vextension.document.DocumentManagement
import eu.vironlab.vextension.document.initDocumentManagement
import java.io.File

object VextensionAPI {

    @JvmStatic
    lateinit var instance: Vextension

    @JvmStatic
    fun initialize(
        vextension: Vextension,
        depenedencyDir: File = File(".libs"),
    ) {
        DependencyLoader.dataPath = depenedencyDir
        instance = vextension
    }

}


interface Vextension {

    var databaseClient: DatabaseClient

}