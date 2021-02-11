package eu.vironlab.vextension.dependency

import java.io.File
import java.net.URL
import java.nio.file.Files


object DependencyLoader {

    var dataPath: File = File("/.libs/")
    var dependencyClassloader: DependencyClassloader = DependencyClassloader()
    const val SUFFIX = ".jar"

    @JvmStatic
    fun require(server: String = Repository.MAVEN_CENTRAL, dependency: Dependency) {
        if (!server.endsWith("/")) {
            server.plus("/")
        }
        val filePath: String =
            dependency.group.replace('.', '/') + '/' + dependency.artifact + '/' + dependency.version
        val fileName: String = dependency.artifact + '-' + dependency.version + SUFFIX

        val folder: File = File(dataPath, filePath)
        val dest = File(folder, fileName)

        try {
            if (!dest.exists()) {
                println("[MCDCS] »» Downloading library $fileName !")
                dest.parentFile.mkdirs()
                val requestURL = URL(server.toString() + filePath + "/" + fileName)
                Files.copy(requestURL.openStream(), dest.toPath())
            }
            try {
                dependencyClassloader.addJarToClasspath(dest)
            } catch (ex: Exception) {
                ex.printStackTrace()
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
    }

    @JvmStatic
    fun require(coords: String) {
        require(server = Repository.MAVEN_CENTRAL, coords = coords)
    }

    @JvmStatic
    fun require(server: String, coords: String) {
        val split = coords.split(":".toRegex()).toTypedArray()
        if (split.size != 3) {
            println("Wrong Library input... StringExample: 'groupid:artifactid:version' Given: '$coords'")
        }
        require(server, Dependency(split[0], split[1], split[2]))
    }

    object Repository {
        const val MAVEN_CENTRAL = "https://repo1.maven.org/maven2/"

        const val JCENTER = "https://jcenter.bintray.com/"

        const val VIRONLAB_SNAPSHOT = "https://repo.vironlab.eu/repository/snapshot/"
    }

}