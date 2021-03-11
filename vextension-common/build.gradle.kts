dependencies {
    implementation(findProperty("coords_mongo").toString())
    implementation(findProperty("coords_gson").toString())
    implementation(findProperty("coords_jackson_xml").toString())
    implementation(findProperty("coords_jackson").toString())
    implementation(findProperty("coords_snakeyml").toString())
}