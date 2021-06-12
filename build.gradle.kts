import kotlin.collections.*

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.20")
        classpath("com.github.jengelman.gradle.plugins:shadow:6.1.0")
    }
}

//Define Plugins
plugins {
    id("java")
    id("maven")
    id("com.github.johnrengelman.shadow") version "6.1.0"
    kotlin("jvm") version "1.4.31"
    kotlin("kapt") version "1.4.31"
    id("org.jetbrains.dokka") version "1.4.20"
}

//Configure build of docs
tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(File(rootProject.buildDir.path, "vextension-v1.0.0"))
}

//Define Variables for all Projects
allprojects {

    //Define Repositorys
    repositories {
        mavenCentral()
        jcenter()
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.destroystokyo.com/repository/maven-public/")
        maven("https://repo.velocitypowered.com/releases/")
        maven("https://libraries.minecraft.net/")
        maven("https://repo.spongepowered.org/maven")
        maven("https://repo.vironlab.eu/repository/snapshot/")
        maven("https://repo.thesimplecloud.eu/artifactory/list/gradle-release-local/")
        maven("https://repo.cloudnetservice.eu/repository/snapshots/")
    }

    //Define Version and Group
    this.group = findProperty("group").toString()
    this.version = findProperty("version").toString()

}

//Default configuration for each module
subprojects {
    apply(plugin = "java")
    apply(plugin = "maven")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.dokka")

    //Define Dependencies for all Modules
    dependencies {
        shadow("org.jetbrains.kotlin:kotlin-stdlib:1.4.31")
        shadow("org.jetbrains.kotlin:kotlin-serialization:1.4.31")
        shadow("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    }

	   if (System.getProperty("publishName") != null && System.getProperty("publishPassword") != null) {
        publishing {
            publications {
                create<MavenPublication>(project.name) {
                    artifact("${project.buildDir}/libs/${project.name}-sources.jar") {
                        extension = "sources"
                    }
                    artifact("${project.buildDir}/libs/${project.name}.jar") {
                        extension = "jar"
                    }
                    groupId = findProperty("group").toString()
                    artifactId = project.name
                    version = findProperty("version").toString()
                    pom {
                        name.set(project.name)
                        url.set("https://github.com/VironLab/Vextension")
                        properties.put("inceptionYear", "2021")
                        licenses {
                            license {
                                name.set("General Public License (GPL v3.0)")
                                url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                                distribution.set("repo")
                            }
                        }
                        developers {
                            developer {
                                id.set("Infinity_dev")
                                name.set("Florin Dornig")
                                email.set("infinitydev@vironlab.eu")
                            }
                            developer {
                                id.set("SteinGaming")
                                name.set("Danial Daryab")
                                email.set("steingaming@vironlab.eu")
                            }
                        }
                    }
                }
                    repositories {
                        maven("https://repo.vironlab.eu/repository/maven-snapshot/") {
                            this.name = "vironlab-snapshot"
                            credentials {
                                this.password = System.getProperty("publishPassword")
                                this.username = System.getProperty("publishName")
                            }
                        }
                    }
            }

    tasks {
        //Set the Name of the Sources Jar
        val sourcesJar by creating(Jar::class) {
            archiveFileName.set("${project.name}-sources.jar")
        }

        shadowJar {
            //Set the Name of the Output File
            archiveFileName.set("${project.name}-full.jar")

            //Configure Shadow
            configurations = listOf(project.configurations.shadow.get())
            exclude("META-INF/**")

            //Include Commons
            if (project.name != "vextension-common") {
                dependsOn(":vextension-common:build")
                val buildDir = project(":vextension-common").buildDir.path
                from("$buildDir/classes/java/main") {
                    include("**")
                }
                from("$buildDir/classes/kotlin/main") {
                    include("**")
                }
                from("$buildDir/resources/main") {
                    include("**")
                }
            }

            doFirst {
                //Set Manifest
                manifest {
                    attributes["Implementation-Title"] = project.name
                    attributes["Implementation-Version"] = findProperty("version").toString()
                    attributes["Specification-Version"] = findProperty("version").toString()
                    attributes["Implementation-Vendor"] = "VironLab.eu"
                    attributes["Built-By"] = System.getProperty("user.name")
                    attributes["Build-Jdk"] = System.getProperty("java.version")
                    attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"
                }
            }
        }

        jar {
            archiveFileName.set("${project.name}.jar")
            doFirst {
                //Set Manifest
                manifest {
                    attributes["Implementation-Title"] = project.name
                    attributes["Implementation-Version"] = findProperty("version").toString()
                    attributes["Specification-Version"] = findProperty("version").toString()
                    attributes["Implementation-Vendor"] = "VironLab.eu"
                    attributes["Built-By"] = System.getProperty("user.name")
                    attributes["Build-Jdk"] = System.getProperty("java.version")
                    attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"
                }
            }
            doLast {
                //Generate the Pom file for the Repository
                maven.pom {
                    withGroovyBuilder {
                        "project" {
                            groupId = "eu.vironlab.vextension"
                            artifactId = project.name
                            version = findProperty("version").toString()
                            this.setProperty("inceptionYear", "2021")
                            "licenses" {
                                "license" {
                                    setProperty("name", "General Public License (GPL v3.0)")
                                    setProperty("url", "https://www.gnu.org/licenses/gpl-3.0.txt")
                                    setProperty("distribution", "repo")
                                }
                            }
                            "developers" {
                                "developer" {
                                    setProperty("id", "Infinity_dev")
                                    setProperty("name", "Florin Dornig")
                                    setProperty("email", "infinitydev@vironlab.eu")
                                }
                                "developer" {
                                    setProperty("id", "SteinGaming")
                                    setProperty("name", "Danial Daryab")
                                    setProperty("email", "steingaming@vironlab.eu")
                                }
                            }
                        }
                    }

                }.writeTo("build/pom/pom.xml")
            }
            //Include Commons
            if (project.name != "vextension-common") {
                dependsOn(":vextension-common:build")
                val buildDir = project(":vextension-common").buildDir.path
                from("$buildDir/classes/java/main") {
                    include("**")
                }
                from("$buildDir/classes/kotlin/main") {
                    include("**")
                }
                from("$buildDir/resources/main") {
                    include("**")
                }
            }
        }

        withType<JavaCompile> {
            this.options.encoding = "UTF-8"
        }
    }


}

