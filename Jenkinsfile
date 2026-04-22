pipeline {
    agent any

    tools {
        maven 'Maven 3.9'  // must match the name in Jenkins Tools
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }

    post {
        always { echo 'Pipeline complete' }
        failure { echo 'Tests failed!' }
        success { echo 'All tests passed!' }
    }
}
