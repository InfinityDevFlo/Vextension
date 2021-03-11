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
                    archiveArtifacts artifacts: 'vextension-discord/build/libs/vextension-discord.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-cli/build/libs/vextension-cli.jar', fingerprint: true
                }
            }
        }
        stage("Docs") {
            steps {
                sh "./gradlew dokkaHtmlMultiModule";
                sh "rm -r /var/www/docs/vextension"
                sh "mkdir /var/www/docs/vextension"
                sh "cp -r build/vextension /var/www/docs/"
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
                    archiveArtifacts artifacts: 'vextension-discord/build/libs/vextension-discord-sources.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-cli/build/libs/vextension-cli-sources.jar', fingerprint: true
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
                                                    artifactId: "vextension-cli",
                                                    classifier: '',
                                                    file      : "vextension-cli/build/libs/vextension-cli.jar",
                                                    type      : "jar"
                                            ],
                                            [
                                                    artifactId: "vextension-cli",
                                                    classifier: 'sources',
                                                    file      : "vextension-cli/build/libs/vextension-cli-sources.jar",
                                                    type      : "jar"
                                            ],
                                            [
                                                    artifactId: "vextension-cli",
                                                    classifier: '',
                                                    file      : "vextension-cli/build/pom/pom.xml",
                                                    type      : "pom"
                                            ],
                                            [
                                                    artifactId: "vextension-discord",
                                                    classifier: '',
                                                    file      : "vextension-discord/build/libs/vextension-discord.jar",
                                                    type      : "jar"
                                            ],
                                            [
                                                    artifactId: "vextension-discord",
                                                    classifier: 'sources',
                                                    file      : "vextension-discord/build/libs/vextension-discord-sources.jar",
                                                    type      : "jar"
                                            ],
                                            [
                                                    artifactId: "vextension-discord",
                                                    classifier: '',
                                                    file      : "vextension-discord/build/pom/pom.xml",
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
