//Repositorys for Plugins
pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
        mavenCentral()
    }
}

//Name of Project
rootProject.name = "Vextension"

//List of all Submodules

//Common Module
include("vextension-common")

//Minecraft Modules
include("vextension-minecraft-server")
include("vextension-minecraft-proxy")

    
