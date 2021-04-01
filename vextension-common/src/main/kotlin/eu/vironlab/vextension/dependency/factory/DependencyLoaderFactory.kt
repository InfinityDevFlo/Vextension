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

import eu.vironlab.vextension.dependency.DependencyClassLoader
import eu.vironlab.vextension.dependency.DependencyLoader
import eu.vironlab.vextension.dependency.Repository
import eu.vironlab.vextension.factory.Factory
import java.io.File

class DependencyLoaderFactory(val libDir: File) : Factory<DependencyLoader> {

    private val repositories: MutableList<Repository> = mutableListOf()
    private var classLoader: DependencyClassLoader? = null

    fun addRepository(name: String, url: String): DependencyLoaderFactory {
        if (!url.endsWith("/")) {
            url.plus("/")
        }
        this.repositories.add(RepositoryImpl(name, url))
        return this
    }

    fun setClassLoader(classLoader: DependencyClassLoader) {
        this.classLoader = classLoader
    }

    fun addMavenCentral(): DependencyLoaderFactory {
        this.repositories.add(RepositoryImpl("maven-central", "https://repo1.maven.org/maven2/"))
        return this
    }

    fun addJCenter(): DependencyLoaderFactory {
        this.repositories.add(RepositoryImpl("jcenter", "https://jcenter.bintray.com/"))
        return this
    }

    fun addVironLabSnapshot(): DependencyLoaderFactory {
        this.repositories.add(RepositoryImpl("vironlab-snapshot", "https://repo.vironlab.eu/repository/snapshot/"))
        return this
    }

    override fun create(): DependencyLoader {
        return DependencyLoaderImpl(this.libDir, repositories, classLoader)
    }

}

fun createDependencyLoader(libDir: File, init: DependencyLoaderFactory.() -> Unit): DependencyLoader {
    val factory: DependencyLoaderFactory = DependencyLoaderFactory(libDir)
    factory.init()
    return factory.create()
}
