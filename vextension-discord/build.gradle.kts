dependencies {
    implementation(project(":vextension-common"))
    kapt(project(":vextension-common"))
    implementation(findProperty("coords_jda").toString())
    implementation(findProperty("coords_gson").toString())
}

tasks.jar {
    val proj = ":vextension-common"

    dependsOn("$proj:build")

    val buildDir = project(proj).buildDir.path
    from("$buildDir/classes/java/main") {
        include("**")
    }
    from("$buildDir/classes/kotlin/main") {
        include("**")
    }
    from("$buildDir/resources/main") {
        include("**")
    }
}