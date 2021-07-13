/**
 * Copyright © 2020 | vironlab.eu | Licensed under the GNU General Public license Version 3<p>
 * <p>
 * ___    _______                        ______         ______  <p>
 * __ |  / /___(_)______________ _______ ___  / ______ ____  /_ <p>
 * __ | / / __  / __  ___/_  __ \__  __ \__  /  _  __ `/__  __ \<p>
 * __ |/ /  _  /  _  /    / /_/ /_  / / /_  /___/ /_/ / _  /_/ /<p>
 * _____/   /_/   /_/     \____/ /_/ /_/ /_____/\__,_/  /_.___/ <p>
 * <p>
 * ____  _______     _______ _     ___  ____  __  __ _____ _   _ _____ <p>
 * |  _ \| ____\ \   / / ____| |   / _ \|  _ \|  \/  | ____| \ | |_   _|<p>
 * | | | |  _|  \ \ / /|  _| | |  | | | | |_) | |\/| |  _| |  \| | | |  <p>
 * | |_| | |___  \ V / | |___| |__| |_| |  __/| |  | | |___| |\  | | |  <p>
 * |____/|_____|  \_/  |_____|_____\___/|_|   |_|  |_|_____|_| \_| |_|  <p>
 * <p>
 * <p>
 * This program is free software: you can redistribute it and/or modify<p>
 * it under the terms of the GNU General Public License as published by<p>
 * the Free Software Foundation, either version 3 of the License, or<p>
 * (at your option) any later version.<p>
 * <p>
 * This program is distributed in the hope that it will be useful,<p>
 * but WITHOUT ANY WARRANTY; without even the implied warranty of<p>
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<p>
 * GNU General Public License for more details.<p>
 * <p>
 * You should have received a copy of the GNU General Public License<p>
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.<p>
 *<p>
 *   Creation: Donnerstag 08 Juli 2021 22:55:43<p>
 *<p>
 * <p>
 * Contact:<p>
 * <p>
 * Discordserver:   https://discord.gg/wvcX92VyEH<p>
 * Website:         https://vironlab.eu/ <p>
 * Mail:            contact@vironlab.eu<p>
 * <p>
 */

package eu.vironlab.vextension.command;

import eu.vironlab.vextension.command.annotation.CommandPath
import eu.vironlab.vextension.command.argument.CommandArgument
import eu.vironlab.vextension.command.context.CommandContext
import eu.vironlab.vextension.command.executor.CommandExecutor
import eu.vironlab.vextension.command.source.CommandSource
import eu.vironlab.vextension.extension.random
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import eu.vironlab.vextension.command.Command as CommandInterface
import eu.vironlab.vextension.command.annotation.Command as CommandAnnotation
import eu.vironlab.vextension.command.annotation.CommandArgument as CommandArgumentAnnotation

abstract class AbstractCommandManager<S : CommandSource, C : CommandContext<S>>(contextClass: Class<C>) :
    CommandManager<S, C> {

    private val validParameterTypes = listOf(
        String::class.java,
        Array<String>::class.java,
        Int::class.java,
        Long::class.java,
        Boolean::class.java,
        Enum::class.java,
        contextClass
    )
    val aliases: MutableMap<String, String> = ConcurrentHashMap<String, String>()
    private val validParameterTypesWithoutAnnotation = listOf(Array<String>::class.java, contextClass)
    override val commands: MutableMap<String, Command> = ConcurrentHashMap()

    override fun register(cmd: CommandExecutor<S, C>): Boolean {
        val annotation: CommandAnnotation = cmd::class.java.getAnnotation(CommandAnnotation::class.java)
            ?: throw IllegalStateException("Cannot Register Command without ${CommandAnnotation::class.java.canonicalName} Annotation")
        val methods = cmd::class.java.declaredMethods.filter { it.isAnnotationPresent(CommandPath::class.java) }
        val command = Command(
            mutableListOf(),
            cmd,
            annotation.name,
            annotation.aliases,
            CopyOnWriteArrayList()
        )
        for (method in methods) {
            val parameters = method.parameters

            //Check Parameter compatibility
            parameters.filter { param ->
                if (param.getAnnotation(CommandArgumentAnnotation::class.java) != null) {
                    !this.validParameterTypes.contains(param.type) || !validParameterTypes.any {
                        it.isAssignableFrom(
                            param.type
                        )
                    }
                } else {
                    !this.validParameterTypesWithoutAnnotation.contains(param.type) || !validParameterTypesWithoutAnnotation.any {
                        it.isAssignableFrom(
                            param.type
                        )
                    }
                }
            }.also { invalidParameters ->
                if (invalidParameters.isNotEmpty()) {
                    invalidParameters.forEach { invalidParam ->
                        throw IllegalArgumentException("The ParameterType ${invalidParam.name} (${invalidParam.type}) is Invalid")
                    }
                }
            }

            //Check for Array
            val filtered = parameters.filter { it.type == Array<String>::class.java }
            if (filtered.isNotEmpty()) {
                if (parameters.last() != filtered.first()) {
                    throw IllegalStateException("The String Array have to be the last parameter")
                }
                if (filtered.size > 1) {
                    throw IllegalStateException("You can use the String Array only once")
                }
            }

            method.isAccessible = true
            val pathAnnotation = method.getAnnotation(CommandPath::class.java)
            val path = pathAnnotation.path.split(" ")
            if (path.isEmpty()) {
                throw IllegalStateException("Cannot parse path from ${method.name}")
            }
            val pathMap = ConcurrentHashMap<String, Class<*>>()
            if (parameters.filter { it.isAnnotationPresent(CommandArgumentAnnotation::class.java) }.size != path.filter {
                    isDynamicPathContent(
                        it
                    )
                }.size) {
                throw IllegalStateException("The Amount of Annotated Parameters and Dynamic Parts of the Command are not the same")
            }

            val parameterList = mutableListOf<ParameterData>()
            for (param in parameters) {
                val argumentAnnotation = param.getAnnotation(CommandArgumentAnnotation::class.java) ?: continue
                parameterList.add(ParameterData(param.type, argumentAnnotation))
            }


            command.subcommands.add(SubCommandInfo(method, pathMap, parameterList))

        }
        var commandId = String.random(16)
        while (commands.containsKey(commandId)) {
            commandId = String.random(16)
        }
        this.aliases.also { aliasMap ->
            aliasMap[annotation.name] = commandId
            for (alias in command.aliases) {
                aliasMap[alias] = commandId
            }
        }
        this.commands[commandId] = command
        return true
    }

    private fun isDynamicPathContent(str: String) = str.startsWith("<") && str.endsWith(">")

    override fun parseLine(line: String, source: S): Boolean {
        TODO("Implement")
    }

    inner class Command(
        override val arguments: List<CommandArgument>,
        override val executor: CommandExecutor<S, C>,
        override val name: String,
        override val aliases: Array<String>,
        val subcommands: MutableList<SubCommandInfo>
    ) : CommandInterface<S, C>

    inner class ParameterData(val type: Class<*>, val info: CommandArgumentAnnotation)

    inner class SubCommandInfo(
        val method: Method,
        val path: Map<String, Class<*>>,
        val parameterInfo: List<ParameterData>
    )

}