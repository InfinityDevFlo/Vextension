dependencies {
    compileOnly(project(":vextension-common"))
    compileOnly(findProperty("coords_bukkit").toString())
    compileOnly(findProperty("coords_sponge").toString())
}