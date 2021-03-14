dependencies {
    implementation(project(":vextension-common"))
    implementation(findProperty("coords_bukkit").toString())
    implementation(findProperty("coords_sponge").toString())
    kapt(findProperty("coords_sponge").toString())
}
