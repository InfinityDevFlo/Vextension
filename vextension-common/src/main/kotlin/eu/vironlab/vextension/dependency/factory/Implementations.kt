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

import eu.vironlab.vextension.dependency.Dependency
import eu.vironlab.vextension.dependency.DependencyClassLoader
import eu.vironlab.vextension.dependency.DependencyLoader
import eu.vironlab.vextension.dependency.Repository
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.net.MalformedURLException
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files

internal class RepositoryImpl(override val url: String, override val name: String) : Repository

internal class DependencyImpl(
    override val groupId: String,
    override val artifactId: String,
    override val version: String
) : Dependency

internal class DependencyLoaderImpl(override val libDir: File, override val repositories: Collection<Repository>) : DependencyLoader {

    override val classLoader: DependencyClassLoader = DependencyClassloaderImpl()

    init {
        libDir.mkdirs()
    }

    override fun require(coords: String) {
        val split = coords.split(":".toRegex()).toTypedArray()
        if (split.size != 3) {
            throw java.lang.IllegalStateException("Wrong Library input... StringExample: 'groupid:artifactid:version' Given: '$coords'")
        }
        require(DependencyImpl(split[0], split[1], split[2]))
    }

    override fun require(dependency: Dependency) {
        val filePath: String =
            dependency.groupId.replace('.', '/') + '/' + dependency.artifactId + '/' + dependency.version
        val fileName: String = dependency.artifactId + '-' + dependency.version + ".jar"

        val folder: File = File(libDir, filePath)
        val dest = File(folder, fileName)

        TODO("Find correct Repository Server")

        /*try {
            if (!dest.exists()) {
                dest.parentFile.mkdirs()
                val requestURL = URL("$server$filePath/$fileName")
                Files.copy(requestURL.openStream(), dest.toPath())
            }
            try {
                classLoader.addJarToClasspath(dest)
            } catch (ex: Exception) {
                ex.printStackTrace()
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }*/
    }

    /**
     * ClassLoader Implementation
     */
    internal inner class DependencyClassloaderImpl : DependencyClassLoader {

        private var addUrl: Method

        private var classLoader: URLClassLoader? = null

        init {
            try {
                openUrlClassLoaderModule()
            } catch (throwable: Throwable) {
            }
            try {
                addUrl = URLClassLoader::class.java.getDeclaredMethod("addURL", *arrayOf(URL::class.java))
                addUrl.setAccessible(true)
            } catch (e: NoSuchMethodException) {
                throw RuntimeException(e)
            }
            if (javaClass.classLoader is URLClassLoader) {
                classLoader = javaClass.classLoader as URLClassLoader
            } else {
                throw IllegalStateException("ClassLoader is not instance of URLClassLoader")
            }
        }

        override fun addJarToClasspath(paramPath: File) {
            try {
                addUrl.invoke(classLoader, paramPath.toURI().toURL())
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException(e)
            } catch (e: MalformedURLException) {
                throw RuntimeException(e)
            }
        }

        @Throws(Exception::class)
        private fun openUrlClassLoaderModule() {
            val moduleClass = Class.forName("java.lang.Module")
            val getModuleMethod: Method = Class::class.java.getMethod("getModule", *arrayOfNulls(0))
            val addOpensMethod: Method = moduleClass.getMethod("addOpens", *arrayOf(String::class.java, moduleClass))
            val urlClassLoaderModule: Any = getModuleMethod.invoke(URLClassLoader::class.java, arrayOfNulls<Any>(0))
            val thisModule: Any = getModuleMethod.invoke(this::class.java, arrayOfNulls<Any>(0))
            addOpensMethod.invoke(
                urlClassLoaderModule,
                arrayOf(URLClassLoader::class.java.getPackage().name, thisModule)
            )
        }


    }

}
