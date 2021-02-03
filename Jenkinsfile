pipeline {
    agent any

    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "repo.vironlab.eu"
        NEXUS_REPOSITORY = "snapshot"
        NEXUS_CREDENTIAL_ID = "jenkins"
    }

    stages {
        stage("Clean") {
            steps {
                sh "chmod +x ./gradlew";
                sh "./gradlew clean";
            }
        }
        stage("Build") {
            steps {
                sh "./gradlew jar";
            }
        }
        stage("Sources") {
            steps {
                sh "./gradlew sourceJar";
            }
        }
        stage("Publish") {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml";
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactExists = fileExists artifactPath;

                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version: ${pom.version}"
                        nexusArtifactUploader {
                            nexusVersion: NEXUS_VERSION
                            protocol: NEXUS_PROTOCOL
                            nexusUrl: NEXUS_URL
                            groupId: pom.groupId
                            version: pom.version
                            repository: NEXUS_REPOSITORY
                            credentialsId: NEXUS_CREDENTIAL_ID
                            artifact {
                                    artifactId: "vextension-core",
                                    type: "jar",
                                    classifier: "",
                                    file: "vextension-core/build/libs/vextension-core.jar"
                            }
                            artifact {
                                    artifactId: "vextension-core",
                                    type: "pom",
                                    classifier: "",
                                    file: "vextension-core/build/pom/pom.xml"
                            }
                        }
                    } else {
                        error "*** File: ${artifactPath}, could not be found"
                    }
                }
            }
        }
    }
}