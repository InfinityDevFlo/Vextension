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
include("vextension-common")

//Discord
include("vextension-discord")

//Minecraft Modules
include("vextension-minecraft-server")
include("vextension-minecraft-proxy")

    
