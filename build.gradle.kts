buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.20")
        classpath("com.github.jengelman.gradle.plugins:shadow:6.1.0")
    }
}

//Define Plugins
plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "6.1.0"
    kotlin("jvm") version "1.5.10"
    id("org.jetbrains.dokka") version "1.4.20"
}

//Configure build of docs
tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(File(rootProject.buildDir.path, "vextension-v2.0.0"))
}
//Define Variables for all Projects
allprojects {
    //Define Repositorys
    repositories {
        for (field in Repositories::class.java.declaredFields) {
            if (field.name != "INSTANCE") {
                println("Added Repository: " + field.get(null))
                maven(field.get(null))
            }
        }
    }

    //Define Version and Group
    this.group = Properties.group
    this.version = Properties.version

}

//Default configuration for each module
subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.dokka")

    //Define Dependencies for all Modules
    dependencies {
        implementation(getDependency("kotlin", "stdlib"))
        implementation(getDependency("kotlin", "serialization"))
        implementation(getDependency("kotlinx", "coroutines-core"))
        testImplementation("org.jetbrains.kotlin:kotlin-test")
    }

    java {
        withSourcesJar()
    }

    if (System.getProperty("publishName") != null && System.getProperty("publishPassword") != null) {
        publishing {
            (components["java"] as AdhocComponentWithVariants).withVariantsFromConfiguration(configurations["shadowRuntimeElements"]) {
                skip()
            }
            publications {
                create<MavenPublication>(project.name) {
                    groupId = Properties.group
                    artifactId = project.name
                    version = Properties.version
                    from(components["java"])
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
        }
    }
    tasks {

        test {
            useJUnitPlatform()
        }

        //Set the Name of the Sources Jar
        kotlinSourcesJar {
            archiveFileName.set("${project.name}-${Properties.version}-${getCommitHash()}-sources.jar")
            destinationDirectory.set(file("$projectDir/../out"))
            doFirst {
                //Set Manifest
                manifest {
                    attributes["Implementation-Title"] = project.name
                    attributes["Implementation-Version"] = Properties.version
                    attributes["Specification-Version"] = Properties.version
                    attributes["Implementation-Vendor"] = "VironLab.eu"
                    attributes["Built-By"] = System.getProperty("user.name")
                    attributes["Build-Jdk"] = System.getProperty("java.version")
                    attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"
                    attributes["VironLab-AppId"] = "vextension"
                    attributes["Commit-Hash"] = getCommitHash()
                }
            }
        }

        shadowJar {
            //Set the Name of the Output File
            archiveFileName.set("${project.name}-${Properties.version}-${getCommitHash()}-full.jar")
            destinationDirectory.set(file("$projectDir/../out"))
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
                    attributes["Implementation-Version"] = Properties.version
                    attributes["Specification-Version"] = Properties.version
                    attributes["Implementation-Vendor"] = "VironLab.eu"
                    attributes["Built-By"] = System.getProperty("user.name")
                    attributes["Build-Jdk"] = System.getProperty("java.version")
                    attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"
                    attributes["VironLab-AppId"] = "vextension"
                    attributes["Commit-Hash"] = getCommitHash()
                }
            }
        }

        jar {
            archiveFileName.set("${project.name}-${Properties.version}-${getCommitHash()}.jar")
            destinationDirectory.set(file("$projectDir/../out"))
            doFirst {
                //Set Manifest
                manifest {
                    attributes["Implementation-Title"] = project.name
                    attributes["Implementation-Version"] = Properties.version
                    attributes["Specification-Version"] = Properties.version
                    attributes["Implementation-Vendor"] = "VironLab.eu"
                    attributes["Built-By"] = System.getProperty("user.name")
                    attributes["Build-Jdk"] = System.getProperty("java.version")
                    attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"
                    attributes["VironLab-AppId"] = "vextension"
                    attributes["Commit-Hash"] = getCommitHash()
                }
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

        compileKotlin {
            kotlinOptions.jvmTarget = "16"
        }

        withType<JavaCompile> {
            this.options.encoding = "UTF-8"
        }
    }
}
