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

import java.net.URL
import java.util.*

object StringExtension {
    val UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

    val LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz".toCharArray()

    val UPPER_LOWER_CHARS = UPPER_CHARS + LOWER_CHARS

    val NUMBERS = "0123456789".toCharArray()

    val CHARS = UPPER_CHARS + LOWER_CHARS + NUMBERS

    val RANDOM: Random = Random()
}

fun String.isClass(): Boolean {
    return try {
        Class.forName(this)
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Check if the String is an Integer
 */
fun String.isInt(): Boolean {
    return try {
        this.toInt()
        true
    } catch (e: Exception) {
        false
    }
}

fun String.isLong(): Boolean {
    return try {
        this.toLong()
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Check if the String is a Boolean
 */
fun String.isBoolean(): Boolean {
    return try {
        this.toBoolean()
        true
    } catch (e: Exception) {
        false
    }
}


fun String.Companion.random(length: Int, numbers: Boolean = true): String {
    val stringBuilder = StringBuilder()
    synchronized(StringExtension::class.java) {
        if (numbers) {
            val size = StringExtension.CHARS.size
            for (i in 0 until length) {
                stringBuilder.append(StringExtension.CHARS[StringExtension.RANDOM.nextInt(size)])
            }
        } else {
            val size = StringExtension.UPPER_LOWER_CHARS.size
            for (i in 0 until length) {
                stringBuilder.append(StringExtension.UPPER_LOWER_CHARS[StringExtension.RANDOM.nextInt(size)])
            }
        }
    }
    return stringBuilder.toString()
}

fun String.containsIgnoreCase(searchQuery: String): Boolean {
    return this.toLowerCase().contains(searchQuery.toLowerCase())
}

/**
 * Check if the String is an URL
 */
fun String.isUrl(): Boolean {
    return try {
        URL(this)
        true
    } catch (e: Exception) {
        false
    }
}

fun String.isUUID(): Boolean {
    return try {
        this.toUUID()
        true
    } catch (e: Exception) {
        false
    }
}

fun String.toUUID(): UUID {
    return if (contains("-")) {
        UUID.fromString(this)
    } else {
        var uuid = ""
        for (i in 0..31) {
            uuid += this[i]
            if (i == 7 || i == 11 || i == 15 || i == 19) {
                uuid = "$uuid-"
            }
        }
        UUID.fromString(uuid)
    }
}