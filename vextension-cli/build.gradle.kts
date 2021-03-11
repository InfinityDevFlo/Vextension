dependencies {
    implementation(findProperty("coords_jline").toString())
    implementation(findProperty("coords_jansi").toString())
    implementation(project(":vextension-common"))
}

