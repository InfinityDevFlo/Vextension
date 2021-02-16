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
            post {
                success {
                    archiveArtifacts artifacts: 'vextension-common/build/libs/vextension-common.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-server/build/libs/vextension-minecraft-server.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-proxy/build/libs/vextension-minecraft-proxy.jar', fingerprint: true
                }
            }
        }
        stage("Docs") {
            steps {
                sh "./gradlew dokkaHtml";
                sh "rm -r /var/www/docs/vextension"
                sh "mkdir /var/www/docs/vextension"
                sh "cp -r vextension-common/build/dokka/html /var/www/docs/vextension/common"
                sh "cp -r vextension-minecraft-server/build/dokka/html /var/www/docs/vextension/minecraft-server"
                sh "cp -r vextension-minecraft-proxy/build/dokka/html /var/www/docs/vextension/minecraft-proxy"
            }
        }
        stage("Sources") {
            steps {
                sh "./gradlew sourcesJar";
            }
            post {
                success {
                    archiveArtifacts artifacts: 'vextension-common/build/libs/vextension-common-sources.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-server/build/libs/vextension-minecraft-server-sources.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-proxy/build/libs/vextension-minecraft-proxy-sources.jar', fingerprint: true
                }
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
                                                    artifactId: "vextension-common",
                                                    classifier: '',
                                                    file      : "vextension-common/build/libs/vextension-common.jar",
                                                    type      : "jar"
                                            ],
                                            [
                                                    artifactId: "vextension-common",
                                                    classifier: 'sources',
                                                    file      : "vextension-common/build/libs/vextension-common-sources.jar",
                                                    type      : "jar"
                                            ],
                                            [
                                                    artifactId: "vextension-common",
                                                    classifier: '',
                                                    file      : "vextension-common/build/pom/pom.xml",
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
                                                    classifier: 'sources',
                                                    file      : "vextension-minecraft-server/build/libs/vextension-minecraft-server-sources.jar",
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
                                                    classifier: 'sources',
                                                    file      : "vextension-minecraft-proxy/build/libs/vextension-minecraft-proxy-sources.jar",
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