/**
 *   Copyright © 2020 | vironlab.eu | Licensed under the GNU General Public license Version 3<p>
 * <p>
 *      ___    _______                        ______         ______  <p>
 *      __ |  / /___(_)______________ _______ ___  / ______ ____  /_ <p>
 *      __ | / / __  / __  ___/_  __ \__  __ \__  /  _  __ `/__  __ \<p>
 *      __ |/ /  _  /  _  /    / /_/ /_  / / /_  /___/ /_/ / _  /_/ /<p>
 *      _____/   /_/   /_/     \____/ /_/ /_/ /_____/\__,_/  /_.___/ <p>
 *<p>
 *    ____  _______     _______ _     ___  ____  __  __ _____ _   _ _____ <p>
 *   |  _ \| ____\ \   / / ____| |   / _ \|  _ \|  \/  | ____| \ | |_   _|<p>
 *   | | | |  _|  \ \ / /|  _| | |  | | | | |_) | |\/| |  _| |  \| | | |  <p>
 *   | |_| | |___  \ V / | |___| |__| |_| |  __/| |  | | |___| |\  | | |  <p>
 *   |____/|_____|  \_/  |_____|_____\___/|_|   |_|  |_|_____|_| \_| |_|  <p>
 *<p>
 *<p>
 *   This program is free software: you can redistribute it and/or modify<p>
 *   it under the terms of the GNU General Public License as published by<p>
 *   the Free Software Foundation, either version 3 of the License, or<p>
 *   (at your option) any later version.<p>
 *<p>
 *   This program is distributed in the hope that it will be useful,<p>
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of<p>
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<p>
 *   GNU General Public License for more details.<p>
 *<p>
 *   You should have received a copy of the GNU General Public License<p>
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.<p>
 *<p>
 *   Contact:<p>
 *<p>
 *     Discordserver:   https://discord.gg/wvcX92VyEH<p>
 *     Website:         https://vironlab.eu/ <p>
 *     Mail:            contact@vironlab.eu<p>
 *<p>
 */

package eu.vironlab.vextension.dependency.impl

import com.google.gson.JsonArray
import eu.vironlab.vextension.dependency.*
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.document.impl.DefaultDocumentManagement
import eu.vironlab.vextension.rest.RestUtil
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.jar.JarFile

internal class DefaultDependencyLoader(val libDir: File, val repositories: MutableMap<String, String>) :
    DependencyLoader {

    val queue: Queue<DownloadableJar> = ConcurrentLinkedQueue<DownloadableJar>()
    override fun addRepository(name: String, url: URL) {
        if (this.repositories.contains(name)) {
            return
        }
        this.repositories[name] = url.toString().let {
            if (!it.endsWith("/")) {
                it.plus("/")
            }
            it
        }
    }

    override fun isRepositoryExists(name: String) = this.repositories[name] != null
    override fun getRepositoryName(url: URL): String? = this.repositories.filter {
        it.value == url.toString().let {
            if (!it.endsWith("/")) {
                it.plus("/")
            }
            it
        }
    }.keys.firstOrNull()

    override fun addToQueue(dependency: Dependency, subdependencies: Boolean): DependencyLoader {
        val filePath: String =
            dependency.groupId.replace('.', '/') + '/' + dependency.artifactId + '/' + dependency.version
        val fileName: String = dependency.artifactId + '-' + dependency.version + ".jar"
        val folder: File = File(libDir, filePath)
        val dest = File(folder, fileName)
        var server: String? = null
        for (it in repositories) {
            if (RestUtil.getStatusCode(URL("${it.value}$filePath/$fileName")).equals(200)) {
                server = "${it.value}$filePath/$fileName"
                break
            } else {
                if (RestUtil.getStatusCode(URL("${it.value}$filePath/maven-metadata.xml")).equals(200)) {
                    val meta: Document =
                        RestUtil.DEFAULT_CLIENT.getXmlDocument("${it.value}$filePath/maven-metadata.xml").get()
                    val newFileName =
                        dependency.artifactId + "-" + meta.getDocument("versioning")?.getDocument("snapshotVersions")
                            ?.getDocument("snapshotVersion")?.getString("value") + ".jar"
                    server = "${it.value}$filePath/$newFileName"
                    break
                }
            }
        }
        if (server == null) {
            throw NoRepositoryFoundException("Cannot find the artifact ${dependency.toCoords()}")
        }
        if (subdependencies) {
            val pom = RestUtil.DEFAULT_CLIENT.getXmlDocument(server.substring(0, server.length - 3) + "pom")
                .orElseGet { null } ?: run {
                this.queue.offer(DownloadableJar(URL("$server"), dest, folder))
                return this
            }
            val depends: JsonArray = pom.getDocument("dependencies")?.getJsonArray("dependency") ?: run {
                this.queue.offer(DownloadableJar(URL("$server"), dest, folder))
                return this
            }
            for (depend in depends) {
                val dependen =
                    DefaultDocumentManagement.GSON.fromJson(depend.asJsonObject.asString, Dependency::class.java)
                if (dependen.scope == "runtime") {
                    addToQueue(dependency, subdependencies)
                }
            }
        }
        this.queue.offer(DownloadableJar(URL("$server"), dest, folder))
        return this
    }

    override fun addToQueue(gradle: String, subdependencies: Boolean): DependencyLoader {
        val split = gradle.split(":").toTypedArray()
        if (split.size != 3) {
            throw java.lang.IllegalStateException("Wrong Library input... StringExample: 'groupid:artifactid:version' Given: '$gradle'")
        }
        addToQueue(Dependency(split[0], split[1], split[2], "runtime"), subdependencies)
        return this
    }

    override fun addToQueue(name: String, server: URL): DependencyLoader =
        this.queue.offer(DownloadableJar(server, File(libDir, name), null)).let { this }

    override fun addQueueWithAgent() = downloadQueue().let { downloaded ->
        downloaded.forEach { file ->
            DependencyAgent.appendJarFile(JarFile(file))
        }
    }

    override fun createClassLoader(): URLClassLoader =
        DependencyClassLoader(downloadQueue().map { it.toURI().toURL() }.toTypedArray())

    private fun downloadQueue(): ArrayList<File> {
        val urls = arrayListOf<File>()
        for (entry in queue) {
            if (!entry.targetFile.exists() || entry.targetFile.name.lowercase(Locale.getDefault())
                    .contains("snapshot")
            ) {
                if (entry.dir != null) {
                    if (!entry.dir.exists()) {
                        Files.createDirectories(entry.dir.toPath())
                    }
                }
                val stream = entry.url.openStream()
                Files.copy(stream, entry.targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                stream.close()
            }
            urls.add(entry.targetFile)
        }
        return urls
    }

    internal inner class DownloadableJar(val url: URL, val targetFile: File, val dir: File? = null)
}
