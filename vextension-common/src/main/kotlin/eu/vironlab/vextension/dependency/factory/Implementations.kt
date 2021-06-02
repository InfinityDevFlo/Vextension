/**
 *   Copyright © 2020 | vironlab.eu | All Rights Reserved.<p>
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

package eu.vironlab.vextension.dependency.factory

import eu.vironlab.vextension.concurrent.task.QueuedTask
import eu.vironlab.vextension.concurrent.task.queueTask
import eu.vironlab.vextension.dependency.*
import eu.vironlab.vextension.dependency.exception.NoRepositoryFoundException
import eu.vironlab.vextension.rest.RestUtil
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.jar.JarFile

internal class RepositoryImpl(override val url: String, override val name: String) : Repository

internal class DependencyImpl(
    override val groupId: String,
    override val artifactId: String,
    override val version: String,
) : Dependency

internal class DefaultDependencyQueue(override val queue: Queue<DownloadableJar>, val libDir: File) : DependencyQueue {

    override fun download(): QueuedTask<Collection<Throwable>> = queueTask {
        val errors = mutableListOf<Throwable>()
        val iterator = queue.iterator()
        val files: MutableList<File> = mutableListOf()
        while (iterator.hasNext()) {
            val jar = iterator.next()
            if (!jar.targetFile.exists() || jar.targetFile.name.toLowerCase().contains("snapshot")) {
                if (jar.dir != null) {
                    if (!jar.dir!!.exists()) {
                        Files.createDirectories(jar.dir!!.toPath())
                    }
                }
                Files.copy(jar.url.openStream(), jar.targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
            files.add(jar.targetFile)
        }
        addToClassPath(files.toTypedArray())
        errors
    }


    fun addToClassPath(files: Array<File>) {
        val classes: MutableList<String> = mutableListOf()
        for (file in files) {
            classes.addAll(JarFile(file).entries().toList().filter { it.name.endsWith(".class") }
                .map { it.name.replace("/", ".").replace(".class", "") })
        }
        val loader = URLClassLoader(files.map { it.toURI().toURL() }.toTypedArray())
        for (clazz in classes) {
            loader.loadClass(clazz)
            classes -= clazz
        }
    }

}

internal class DependencyLoaderImpl(
    override val libDir: File,
    override val repositories: MutableCollection<Repository>
) :
    DependencyLoader {


    val queue: Queue<DownloadableJar> = ConcurrentLinkedQueue<DownloadableJar>()

    init {
        libDir.mkdirs()
    }

    override fun addRepository(name: String, url: URL) {
        if (this.repositories.contains(name)) {
            return
        }
        this.repositories.add(object : Repository {
            override val url: String = url.toString()
            override val name: String = name
        })
    }

    override fun addToQueue(dependency: Dependency): DependencyLoader {
        val filePath: String =
            dependency.groupId.replace('.', '/') + '/' + dependency.artifactId + '/' + dependency.version
        val fileName: String = dependency.artifactId + '-' + dependency.version + ".jar"
        val folder: File = File(libDir, filePath)
        val dest = File(folder, fileName)
        var server: String? = null
        for (it in repositories) {
            if (RestUtil.getStatusCode(URL("${it.url}$filePath/$fileName")).equals(200)) {
                server = it.url
                break
            }
        }
        if (server == null) {
            throw NoRepositoryFoundException("Cannot find the artifact ${dependency.toCoords()}")
        }
        this.queue.offer(DownloadableJarImpl(URL("$server$filePath/$fileName"), dest, folder))
        return this
    }

    override fun addToQueue(gradle: String): DependencyLoader {
        val split = gradle.split(":").toTypedArray()
        if (split.size != 3) {
            throw java.lang.IllegalStateException("Wrong Library input... StringExample: 'groupid:artifactid:version' Given: '$gradle'")
        }
        addToQueue(DependencyImpl(split[0], split[1], split[2]))
        return this
    }

    override fun addToQueue(name: String, server: URL): DependencyLoader =
        this.queue.offer(DownloadableJarImpl(server, File(libDir, name), null)).let { this }

    override fun createQueue(): DependencyQueue {
        return DefaultDependencyQueue(this.queue, libDir)
    }

    internal inner class DownloadableJarImpl(
        override val url: URL,
        override val targetFile: File,
        override val dir: File?
    ) : DownloadableJar


}
