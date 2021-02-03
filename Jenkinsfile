pipeline {
    agent any

    stages {
        stage('Clean') {
            steps {
                sh 'chmod +x ./gradlew';
                sh './gradlew clean';
            }
        }
        stage('Build') {
            steps {
                sh './gradlew jar';
            }
        }
        stage('Sources') {
            steps {
                sh './gradlew sourceJar';
            }
        }
        stage('Deploy') {
            steps {
                nexusArtifactUploader {
                    nexusVersion('nexus3');
                    protocol('https');
                    nexusUrl('repo.vironlab.eu');
                    groupId('eu.vironlab.vextension');
                    version('1.0.1-SNAPSHOT');
                    repository('Nexus');
                    credentialsId('jenkins');
                    artifact {
                        artifactId('vextension-core');
                        type('jar');
                        classifier('snapshot');
                        file('vextension-core/build/libs/vextension-core.jar');
                    }
                }
            }
        }
    }
}