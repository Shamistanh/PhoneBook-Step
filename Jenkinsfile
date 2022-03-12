pipeline {
    agent any
    tools {
        maven 'apache-maven-3.8.4'
        }
    stages {
        stage ('Compiling') {
            steps {
                    sh 'mvn clean compile'
            }
        }

        stage('Image build') {
            steps {
                 script {
                     sh 'mvn clean install && docker build -t shamistanhuseynov1999/app-book .'
                 }
            }
        }
        stage('Docker push') {
            steps {
                 script {
                    sh 'docker login -u shamistanhuseynov1999 -p @Sh7513244'
                    sh 'docker push  shamistanhuseynov1999/app-book'
                 }
            }
        }
    }
}