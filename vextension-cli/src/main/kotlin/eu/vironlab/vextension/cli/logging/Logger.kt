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

package eu.vironlab.vextension.cli.logging

/**
 * This is the Logger wich is used for the Console of the CLI
 */
interface Logger {

    /**
     * Log [message] with [type] wich came from [caller] and [throwable]
     */
    fun log(message: String, type: LogType, caller: Class<*>?, throwable: Throwable?)

    /**
     * Log [message] with [type] from [caller]
     */
    fun log(message: String, type: LogType, caller: Class<*>?) {
        log(message, type, caller, null)
    }

    /**
     * Log [message] with [type] from [throwable]
     */
    fun log(message: String, type: LogType, throwable: Throwable?) {
        log(message, type, null, throwable)
    }

    /**
     * Log [message] with [type]
     */
    fun log(message: String, type: LogType) {
        log(message, type, null, null)
    }

    /**
     * Log multiple [messages] with [type]
     */
    fun log(messages: Array<String>, type: LogType) {
        for (message in messages) {
            log(message, type, null, null)
        }
    }

    /**
     * Log [message] with the Logging Type INFO
     */
    fun info(message: String) {
        log(message, LogType.INFO)
    }

    /**
     * Log multiple [messages] with Type INFO
     */
    fun info(messages: Array<String>) {
        for (message in messages) {
            log(message, LogType.INFO)
        }
    }

    /**
     * Log [message] with type INFO from [caller]
     */
    fun info(message: String, caller: Class<*>) {
        log(message, LogType.INFO, caller)
    }

    /**
     * Log [message] with Type INFO from [caller] and [throwable]
     */
    fun info(message: String, caller: Class<*>, throwable: Throwable) {
        log(message, LogType.INFO, caller, throwable)
    }

    /**
     * Log [message] with Type INFO from [throwable]
     */
    fun info(message: String, throwable: Throwable) {
        log(message, LogType.INFO, throwable)
    }


    /**
     * Log [message] with the Logging Type ERROR
     */
    fun error(message: String) {
        log(message, LogType.ERROR)
    }

    /**
     * Log multiple [messages] with Type ERROR
     */
    fun error(messages: Array<String>) {
        for (message in messages) {
            log(message, LogType.ERROR)
        }
    }

    /**
     * Log [message] with type ERROR from [caller]
     */
    fun error(message: String, caller: Class<*>) {
        log(message, LogType.ERROR, caller)
    }

    /**
     * Log [message] with Type ERROR from [caller] and [throwable]
     */
    fun error(message: String, caller: Class<*>, throwable: Throwable) {
        log(message, LogType.ERROR, caller, throwable)
    }

    /**
     * Log [message] with Type ERROR from [throwable]
     */
    fun error(message: String, throwable: Throwable) {
        log(message, LogType.ERROR, throwable)
    }

    /**
     * Log [message] with the Logging Type WARN
     */
    fun warn(message: String) {
        log(message, LogType.WARN)
    }

    /**
     * Log multiple [messages] with Type WARN
     */
    fun warn(messages: Array<String>) {
        for (message in messages) {
            log(message, LogType.WARN)
        }
    }

    /**
     * Log [message] with type WARN from [caller]
     */
    fun warn(message: String, caller: Class<*>) {
        log(message, LogType.WARN, caller)
    }

    /**
     * Log [message] with Type WARN from [caller] and [throwable]
     */
    fun warn(message: String, caller: Class<*>, throwable: Throwable) {
        log(message, LogType.WARN, caller, throwable)
    }

    /**
     * Log [message] with Type WARN from [throwable]
     */
    fun warn(message: String, throwable: Throwable) {
        log(message, LogType.WARN, throwable)
    }
    
}