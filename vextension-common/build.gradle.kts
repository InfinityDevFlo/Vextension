dependencies {
    //Database
    implementation(getDependency("database", "mongo"))
    implementation(getDependency("database", "hikari"))
    implementation(getDependency("database", "mariadb"))
    implementation(getDependency("database", "h2"))

    //Inject
    implementation(getDependency("google", "guice"))

    //Document
    implementation(getDependency("google", "gson"))
    implementation(getDependency("jackson", "xml"))
    implementation(getDependency("jackson", "databind"))
    implementation(getDependency("yaml", "snake"))

}

