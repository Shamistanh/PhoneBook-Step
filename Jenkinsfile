pipeline {
     agent any
    environment{
        PATH = "/opt/apache-maven-3.8.4/bin:$PATH"
        registry = "shamistanhuseynov1999/app-book"
        registryCredential = '4195d665-f6cc-47e0-859b-a4b514a46c55'
        dockerImage = ''
    }
    tools {
        maven 'mvn-installation'
        }
    stages {
           stage ('Git') {
            steps {
                git credentialsId: '95a28875-82c5-43e3-87bb-3485b77944c4', branch: 'main', url: 'https://github.com/Shamistanh/PhoneBook-Step.git'
            }
        }
        stage ('Compiling') {
            steps {
                    sh 'mvn -f backend/pom.xml clean compile'
            }
        }

        stage('Image build') {
            steps {
                 script {
                     sh 'mvn -f backend/pom.xml clean install'
                     dockerImage = docker.build registry
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