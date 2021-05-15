dependencies {
    implementation(findProperty("coords_mongo").toString())
    implementation(findProperty("coords_hikari").toString())
    implementation(findProperty("coords_mariadb").toString())
    implementation(findProperty("coords_h2").toString())
    implementation(findProperty("coords_gson").toString())
    implementation(findProperty("coords_jackson_xml").toString())
    implementation(findProperty("coords_jackson").toString())
    implementation(findProperty("coords_snakeyml").toString())
}

