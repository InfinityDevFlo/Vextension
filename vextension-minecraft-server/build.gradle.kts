dependencies {
    compileOnly(project(":vextension-common"))
    compileOnly(project(":vextension-command"))
    compileOnly(getDependency("minecraft", "bukkit"))
    compileOnly(getDependency("minecraft", "sponge"))
}