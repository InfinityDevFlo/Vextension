dependencies {
    implementation(project(":vextension-common"))
    implementation(findProperty("coords_waterfall").toString())
    implementation(findProperty("coords_velocity").toString())
    kapt(findProperty("coords_velocity").toString())
}
