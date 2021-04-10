dependencies {
    implementation(project(":vextension-common"))
    implementation(findProperty("coords_guice").toString())
    implementation(findProperty("coords_jda").toString())
    implementation(findProperty("coords_gson").toString())
}
