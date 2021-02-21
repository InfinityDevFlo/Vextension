package eu.vironlab.vextension.dependency

import java.io.File
import java.lang.IllegalStateException
import java.net.URL
import java.nio.file.Files

/**
 * WARNING: UNSAFE WHEN GETTING SNAPSHOTS
 */
object DependencyLoader {

    var dataPath: File = File(".libs")
    var dependencyClassloader: DependencyClassloader = DependencyClassloader()
    const val SUFFIX_JAR = ".jar"
    const val SUFFIX_POM = ".pom"

    fun init() {
        dataPath.mkdirs()
    }

    /**
     * Get the [dependency] from [server]
     */
    @JvmStatic
    fun require(server: String = Repository.MAVEN_CENTRAL, dependency: Dependency) {
        if (!server.endsWith("/")) {
            server.plus("/")
        }
        val filePath: String =
            dependency.groupId.replace('.', '/') + '/' + dependency.artifactId + '/' + dependency.version
        val fileName: String = dependency.artifactId + '-' + dependency.version + SUFFIX_JAR

        val folder: File = File(dataPath, filePath)
        val dest = File(folder, fileName)

        try {
            if (!dest.exists()) {
                dest.parentFile.mkdirs()
                val requestURL = URL("$server$filePath/$fileName")
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

    /**
     * Get the Dependency with [coords] from Maven Central Server
     */
    @JvmStatic
    fun require(coords: String) {
        require(server = Repository.MAVEN_CENTRAL, coords = coords)
    }

    /**
     * Get the Dependency with [coords] from [server]
     */
    @JvmStatic
    fun require(server: String, coords: String) {
        val split = coords.split(":".toRegex()).toTypedArray()
        if (split.size != 3) {
            throw IllegalStateException("Wrong Library input... StringExample: 'groupid:artifactid:version' Given: '$coords'")
        }
        require(server, Dependency(split[0], split[1], split[2]))
    }

    object Repository {
        const val MAVEN_CENTRAL = "https://repo1.maven.org/maven2/"

        const val JCENTER = "https://jcenter.bintray.com/"

        const val VIRONLAB_SNAPSHOT = "https://repo.vironlab.eu/repository/snapshot/"
    }


}