pipeline {
    agent any

    tools {
        jdk 'jdk-16'
    }

    environment {
        GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
        VERSION = "2.0.0-SNAPSHOT"
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
        stage("Sources") {
            steps {
                sh "./gradlew kotlinSourcesJar";
            }
        }
        stage("Build ShadowJar") {
            steps {
                sh "./gradlew shadowJar";
            }
        }
        stage("Publish") {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                  sh "./gradlew publish -DpublishPassword=$PASSWORD -DpublishName=$USERNAME"
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: 'vextension-common/build/libs/*.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-command/build/libs/*.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-cli/build/libs/*.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-proxy/build/libs/*.jar', fingerprint: true
                    archiveArtifacts artifacts: 'vextension-minecraft-proxy/build/libs/*.jar', fingerprint: true
                }
            }
        }
    }
}
