dependencies {
    compileOnly(project(":vextension-common"))
    compileOnly(findProperty("coords_waterfall").toString())
    compileOnly(findProperty("coords_velocity").toString())
}
