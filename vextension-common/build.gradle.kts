dependencies {
    //Database
    compileOnly(getDependency("database", "mongo"))
    compileOnly(getDependency("database", "hikari"))
    compileOnly(getDependency("database", "mariadb"))
    compileOnly(getDependency("database", "h2"))

    //Inject
    compileOnly(getDependency("google", "guice"))

    //Document
    compileOnly(getDependency("google", "gson"))
    compileOnly(getDependency("jackson", "xml"))
    compileOnly(getDependency("jackson", "databind"))
    compileOnly(getDependency("yaml", "snake"))

}

