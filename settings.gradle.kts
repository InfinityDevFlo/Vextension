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
include("vextension-minecraft-server")
include("vextension-minecraft-proxy")
include("vextension-common")
include("vextension-discord")
include("vextension-cli")
