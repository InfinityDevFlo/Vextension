pipeline {
    agent any

    tools {
        jdk 'jdk-16'
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
                sh "./gradlew build";
            }
            post {
                success {
                    archiveArtifacts artifacts: 'vextension-common/build/libs/vextension-common.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-server/build/libs/vextension-minecraft-server.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-proxy/build/libs/vextension-minecraft-proxy.jar', fingerprint: true
                }
            }
        }
        stage("Build ShadowJar") {
            steps {
                sh "./gradlew shadowJar";
            }
            post {
                success {
                    archiveArtifacts artifacts: 'vextension-common/build/libs/vextension-common-full.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-server/build/libs/vextension-minecraft-server-full.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-proxy/build/libs/vextension-minecraft-proxy-full.jar', fingerprint: true
                }
            }
        }
        /*stage("Docs") {
            steps {
                sh "./gradlew dokkaHtmlMultiModule";
                sh "rm -r /var/www/docs/vextension-v2.0.0"
                sh "mkdir /var/www/docs/vextension-v2.0.0"
                sh "cp -r build/vextension-v2.0.0 /var/www/docs/"
            }
        }*/
        stage("Sources") {
            steps {
                sh "./gradlew kotlinSourcesJar";
            }
            post {
                success {
                    archiveArtifacts artifacts: 'vextension-common/build/libs/vextension-common-sources.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-server/build/libs/vextension-minecraft-server-sources.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-proxy/build/libs/vextension-minecraft-proxy.jar', fingerprint: true
                }
            }
        }
        stage("Publish") {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                  sh "./gradlew publish -DpublishPassword=$PASSWORD -DpublishName=$USERNAME"
                }
            }
        }
    }
}
