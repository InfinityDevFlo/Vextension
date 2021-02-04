pipeline {
    agent any

    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "localhost:8081"
        NEXUS_REPOSITORY = "snapshot"
        NEXUS_CREDENTIAL_ID = "jenkins"
        PROJECT_VERSION = "1.0.0-SNAPSHOT"
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
                    nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: "eu.vironlab.vextension",
                            version: PROJECT_VERSION,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts:
                                    [
                                            [
                                                    artifactId: "vextension-core",
                                                    classifier: '',
                                                    file      : "vextension-core/build/libs/vextension-core.jar",
                                                    type      : "jar"
                                            ],
                                            [
                                                    artifactId: "vextension-core",
                                                    classifier: '',
                                                    file      : "vextension-core/build/pom/pom.xml",
                                                    type      : "pom"
                                            ],
                                            [
                                                    artifactId: "vextension-minecraft-server",
                                                    classifier: '',
                                                    file      : "vextension-minecraft-server/build/libs/vextension-minecraft-server.jar",
                                                    type      : "jar"
                                            ],
                                            [
                                                    artifactId: "vextension-minecraft-server",
                                                    classifier: '',
                                                    file      : "vextension-minecraft-server/build/pom/pom.xml",
                                                    type      : "pom"
                                            ],
                                            [
                                                    artifactId: "vextension-minecraft-proxy",
                                                    classifier: '',
                                                    file      : "vextension-minecraft-proxy/build/libs/vextension-minecraft-proxy.jar",
                                                    type      : "jar"
                                            ],
                                            [
                                                    artifactId: "vextension-minecraft-proxy",
                                                    classifier: '',
                                                    file      : "vextension-minecraft-proxy/build/pom/pom.xml",
                                                    type      : "pom"
                                            ]
                                    ]
                    );
                }
            }
        }
    }
}