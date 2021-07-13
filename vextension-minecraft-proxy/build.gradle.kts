dependencies {
    compileOnly(project(":vextension-common"))
    compileOnly(project(":vextension-command"))
    compileOnly(getDependency("minecraft", "bungee"))
    compileOnly(getDependency("minecraft", "velocity"))
}
