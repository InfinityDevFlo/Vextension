dependencies {
    compileOnly(project(":vextension-common"))
    implementation(project(":vextension-command"))
}

tasks {
    jar {
        dependsOn(":vextension-command:build")
        val buildDir = project(":vextension-command").buildDir.path
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
}