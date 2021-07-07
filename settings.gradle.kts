//Repositorys for Plugins
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

//Name of Project
rootProject.name = "Vextension"

//List of all Submodules

//Common Module
include("vextension-common")

//Console
include("vextension-cli")
include("vextension-command")

//Minecraft Modules
include("vextension-minecraft-server")
include("vextension-minecraft-proxy")

    
