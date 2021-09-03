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
        }
        stage("Test") {
            steps {
                sh "./gradlew test";
            }
        }
        stage("Docs") {
            steps {
                sh "./gradlew dokkaHtmlMultiModule"
                sh "rm -r /var/docs/vextension-v2.0.0"
                sh "mkdir /var/docs/vextension-v2.0.0"
                sh "cp -r build/vextension-v2.0.0 /var/docs"
            }
        }
        stage("Publish") {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                  sh "./gradlew publish -DpublishPassword=$PASSWORD -DpublishName=$USERNAME"
                }
            }
        }
        stage("Build ShadowJar") {
            steps {
                sh "./gradlew shadowJar";
            }
            post {
                success {
                    archiveArtifacts artifacts: 'vextension-common/build/libs/*.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-command/build/libs/*.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-cli/build/libs/*.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-proxy/build/libs/*.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-server/build/libs/*.jar', fingerprint: true
                }
            }
        }
    }
}
