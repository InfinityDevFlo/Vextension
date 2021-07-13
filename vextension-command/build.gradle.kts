dependencies {
    compileOnly(project(":vextension-common"))
    testImplementation(project(":vextension-common"))
    compileOnly(getDependency("google", "guice"))
    compileOnly(getDependency("google", "gson"))
}