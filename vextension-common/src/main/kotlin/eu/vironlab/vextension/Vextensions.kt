package eu.vironlab.vextension

import java.io.File

object VextensionAPI {

    @JvmStatic
    lateinit var instance: Vextension

    @JvmStatic
    fun initialize(
        vextension: Vextension,
        depenedencyDir: File = File(".libs"),
    ) {
        instance = vextension
    }

}


interface Vextension {


}