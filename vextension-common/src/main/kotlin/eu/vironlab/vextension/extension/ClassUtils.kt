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

package eu.vironlab.vextension.extension

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.jar.JarFile

fun <T> Class<T>.extractFile(path: String, target: File) {
    Files.createFile(target.toPath())
    Files.copy(
        this.getResourceAsStream(name) ?: throw IllegalStateException("The Entry is not in the Jar"), target.toPath()
    )
}

fun <T> Class<T>.extractFolder(path: String, targetDir: File, overwrite: Boolean = true) {
    val target = targetDir.toPath()
    Files.createDirectories(targetDir.toPath())
    val jarPath = this.getResource(path).path ?: run { throw IllegalStateException("The Entry is not in the Jar") }
    JarFile(
        "/${
            jarPath.let {
                it.substring(0, it.lastIndexOf("!"))
                it.substring("file:/".length)
            }
        }"
    ).use {
        it.entries().toList().forEach { entry ->
            if (entry.isDirectory) {
                val dir: Path = target.resolve(entry.name)
                if (!Files.exists(dir)) {
                    Files.createDirectories(dir)
                }
            } else {
                val file = target.resolve(entry.name)
                if (!Files.exists(file) || overwrite) {
                    it.getInputStream(entry).use { `in` -> Files.copy(`in`, file, StandardCopyOption.REPLACE_EXISTING) }
                }
            }
        }
    }
}

