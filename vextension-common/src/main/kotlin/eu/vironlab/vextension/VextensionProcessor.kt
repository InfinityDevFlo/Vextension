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
import eu.vironlab.vextension.database.annotation.DatabaseKey
import eu.vironlab.vextension.database.annotation.DatabaseName
import eu.vironlab.vextension.database.annotation.Ignored
import eu.vironlab.vextension.database.annotation.NewDatabaseObject
import eu.vironlab.vextension.database.info.ObjectInformation
import eu.vironlab.vextension.dependency.Dependency
import java.io.Writer
import java.util.concurrent.TimeUnit
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.FileObject
import javax.tools.StandardLocation


class VextensionProcessor : AbstractProcessor() {

    val GSON = GsonBuilder().serializeNulls().setPrettyPrinting().create()
    private var running: Boolean = false
    private lateinit var filer: Filer
    private lateinit var processingEnvironment: ProcessingEnvironment

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
        processingEnvironment = processingEnv
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        try {
            if (!running) {
                println("________________________________________________________________________________")
                println("                      _                      _               \n" +
                        " /\\   /\\   ___ __  __| |_   ___  _ __   ___ (_)  ___   _ __  \n" +
                        " \\ \\ / /  / _ \\\\ \\/ /| __| / _ \\| '_ \\ / __|| | / _ \\ | '_ \\ \n" +
                        "  \\ V /  |  __/ >  < | |_ |  __/| | | |\\__ \\| || (_) || | | |\n" +
                        "   \\_/    \\___|/_/\\_\\ \\__| \\___||_| |_||___/|_| \\___/ |_| |_|\n" +
                        "                                                             \n"
                )
                println("Starting Vextension Database Processor")
                this.running = true
                for (element: Element in roundEnv.getElementsAnnotatedWith(NewDatabaseObject::class.java)) {
                    if (!element.kind.isClass) {
                        println("Only Classes can be a DatabaseObject")
                    }
                    val time = System.currentTimeMillis()
                    val targetClassName: String = (element as TypeElement).qualifiedName.toString()
                    println("Processing Class ${targetClassName}... ")
                    if (element.enclosedElements.filter { it.getAnnotation(DatabaseKey::class.java) != null }.size != 1) {
                        throw IllegalStateException("Need exaclty ONE Key")
                    }
                    val key = element.enclosedElements.filter { it.getAnnotation(DatabaseKey::class.java) != null }.first()
                    var keyName: String? = null
                    if (key.getAnnotation(DatabaseName::class.java) != null) {
                        keyName = key.getAnnotation(DatabaseName::class.java).name
                    }
                    val ignoredFields: MutableList<String> = mutableListOf()
                    val specificNames: MutableMap<String, String> = mutableMapOf()
                    element.enclosedElements.forEach {
                        if (it.kind.equals(ElementKind.FIELD)) {
                            if (it.getAnnotation(DatabaseName::class.java) != null) {
                                specificNames.put(it.simpleName.toString(), it.getAnnotation(DatabaseName::class.java).name)
                            }
                            if (it.getAnnotation(Ignored::class.java) != null) {
                                ignoredFields.add(it.simpleName.toString())
                            }
                        }
                    }
                    val objectInfo: ObjectInformation = ObjectInformation(
                        keyName ?: key.simpleName.toString(),
                        key.simpleName.toString(),
                        ignoredFields,
                        specificNames,
                    )
                    val fileObject: FileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "eu/vironlab/vextension/database/objects/${targetClassName}.json")
                    val writer: Writer = fileObject.openWriter()
                    writer.write(GSON.toJson(objectInfo))
                    writer.flush()
                    writer.close()
                    println("Finished Processing ${targetClassName} in ${(System.currentTimeMillis() - time)} Millis (${TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - time))} Seconds)")
                }
                println("________________________________________________________________________________")
            }
        }catch(e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(NewDatabaseObject::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion? {
        return SourceVersion.latestSupported()
    }

}