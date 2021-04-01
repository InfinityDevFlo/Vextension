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

package eu.vironlab.vextension

import com.google.gson.GsonBuilder
import com.google.inject.Inject
import eu.vironlab.vextension.database.ORMValidationResult
import eu.vironlab.vextension.database.SerializedORMObjectInfo
import eu.vironlab.vextension.database.annotation.ORMIgnore
import eu.vironlab.vextension.database.annotation.ORMKey
import eu.vironlab.vextension.database.annotation.ORMName
import eu.vironlab.vextension.database.annotation.ORMObject
import java.util.stream.Collectors
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation


class VextensionProcessor : AbstractProcessor() {

    lateinit var filer: Filer
    lateinit var messager: Messager

    override fun init(processingEnv: ProcessingEnvironment) {
        this.filer = processingEnv.filer
        this.messager = processingEnv.messager
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        try {
            val outputMessage: StringBuilder = StringBuilder(
                "\n_______________________________________________________________________ \n\n" +
                        "                        _                      _               \n" +
                        "   /\\   /\\   ___ __  __| |_   ___  _ __   ___ (_)  ___   _ __  \n" +
                        "   \\ \\ / /  / _ \\\\ \\/ /| __| / _ \\| '_ \\ / __|| | / _ \\ | '_ \\ \n" +
                        "    \\ V /  |  __/ >  < | |_ |  __/| | | |\\__ \\| || (_) || | | |\n" +
                        "     \\_/    \\___|/_/\\_\\ \\__| \\___||_| |_||___/|_| \\___/ |_| |_|\n" +
                        "                                                               \n" +
                        "  by VironLab - https://github.com/VironLab \n\n" +
                        " Running VextensionProcessor... \n"
            )

            //############################################################################################################
            // ORM Processing
            outputMessage.append(" Processing ORM Objects... \n");
            for (element: Element in roundEnv.getElementsAnnotatedWith(ORMObject::class.java)) {
                val elementOutPutMessage: StringBuilder =
                    StringBuilder("\n Processing Element: ${element.simpleName} \n")
                if (element.kind !== ElementKind.CLASS) {
                    this.messager
                        .printMessage(
                            Diagnostic.Kind.ERROR, "Only classes can be annotated with "
                                    + ORMObject::class.java.getCanonicalName()
                        )
                    return false
                }

                //Get Keys and Constructors
                val className = (element as TypeElement).qualifiedName.toString()
                val fields = element.enclosedElements.stream()
                    .filter { e -> ElementKind.FIELD == e.kind }
                    .collect(Collectors.toList())
                val constructors = element.enclosedElements.stream()
                    .filter { e -> ElementKind.CONSTRUCTOR == e.kind }
                    .collect(Collectors.toList())

                //Validate Object
                var validationResult: ORMValidationResult =
                    if (fields.filter { it.getAnnotation(ORMKey::class.java) != null }.isEmpty()) {
                        ORMValidationResult.INVALID_KEY
                    } else {
                        ORMValidationResult.OK
                    }

                //Checking Validation
                if (!validationResult.success) {
                    this.messager.printMessage(Diagnostic.Kind.ERROR, "\n" + validationResult.message, element)
                    return false
                }

                //Getting Key Information and Check the Key
                val keyElement = fields.filter { it.getAnnotation(ORMKey::class.java) != null }.first()
                if (keyElement.getAnnotation(ORMIgnore::class.java) != null) {
                    this.messager.printMessage(Diagnostic.Kind.ERROR, "ORMKey Cannot be ignored in $className")
                }
                val keyField: String = keyElement.simpleName.toString()
                val keyName: String = if (keyElement.getAnnotation(ORMName::class.java) != null) {
                    keyElement.getAnnotation(ORMName::class.java).name
                } else {
                    keyField
                }
                elementOutPutMessage.append("    KeyFields: ${keyField} \n")
                elementOutPutMessage.append("    KeyName: ${keyName} \n")

                elementOutPutMessage.append("    Validation: ${validationResult.success} \n")

                //Get ignored fields
                val ignoredFields: MutableCollection<String> = mutableListOf()
                val ignoredFieldsOutPutMessage: StringBuilder = StringBuilder()
                fields.filter { it.getAnnotation(ORMIgnore::class.java) != null }.forEach {
                    ignoredFields.add(it.simpleName.toString())
                    ignoredFieldsOutPutMessage.append(", " + it.simpleName.toString())
                }
                if (!ignoredFieldsOutPutMessage.toString().isEmpty()) {
                    elementOutPutMessage.append(
                        "    IgnoredFields: ${
                            ignoredFieldsOutPutMessage.toString().substring(1)
                        } \n"
                    )
                }


                //Get Renamed Fields
                val renamedFields: MutableMap<String, String> = mutableMapOf()
                val renamedFieldsOutPutMessage: StringBuilder = StringBuilder()
                fields.filter { it.getAnnotation(ORMName::class.java) != null }.forEach {
                    renamedFields.put(it.getAnnotation(ORMName::class.java).name, it.simpleName.toString())
                    renamedFieldsOutPutMessage.append(", ${it.simpleName.toString()} -> ${it.getAnnotation(ORMName::class.java).name}")
                }
                if (!renamedFieldsOutPutMessage.toString().isEmpty()) {
                    elementOutPutMessage.append(
                        "    RenamedFields: ${
                            renamedFieldsOutPutMessage.toString().substring(1)
                        } \n"
                    )
                }

                //Creating Object Information
                val objInfo: SerializedORMObjectInfo = SerializedORMObjectInfo(
                    className,
                    keyName,
                    keyField,
                    ignoredFields,
                    renamedFields
                )
                elementOutPutMessage.append(" \n Creating File: ormobjects/${className}.json \n")
                val fileObj = this.filer.createResource(
                    StandardLocation.CLASS_OUTPUT,
                    "",
                    "ormobjects/${className}.json",
                    element
                )
                val writer = fileObj.openWriter()
                GsonBuilder().setPrettyPrinting().create().toJson(objInfo, writer)
                writer.flush()
                writer.close()
                outputMessage.append(elementOutPutMessage.toString())
            }

            //############################################################################################################


            outputMessage.append(
                "\n_______________________________________________________________________"
            )
            this.messager.printMessage(Diagnostic.Kind.NOTE, outputMessage.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true;
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(ORMObject::class.java.canonicalName)
    }
}
