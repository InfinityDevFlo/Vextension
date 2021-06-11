dependencies {
    compileOnly(findProperty("coords_mongo").toString())
    compileOnly(findProperty("coords_guice").toString())
    compileOnly(findProperty("coords_hikari").toString())
    compileOnly(findProperty("coords_mariadb").toString())
    compileOnly(findProperty("coords_h2").toString())
    compileOnly(findProperty("coords_gson").toString())
    compileOnly(findProperty("coords_jackson_xml").toString())
    compileOnly(findProperty("coords_jackson").toString())
    compileOnly(findProperty("coords_snakeyml").toString())
}

