pipeline {
    agent any

    environment {
		registry = "marcioholanda/app-dcw5"
        registryCredential = "dockerhub_id" 
        dockerImage = ''
    }

    stages {
    	stage('Clone Repository') {
    		steps {  
                git branch: "main", url: 'https://gitlab.com/mhbqtecnologias1/my_dcw5.git'
			}
    	}
    	stage('Build Docker Image') {
            steps{
                script {
                    dockerImage = docker.build registry + ":develop"
                }
            }
        }
    	stage('Send image to Docker Hub') {
            steps{
                script {
                    docker.withRegistry( '', registryCredential) {
                        dockerImage.push()
                    }
                }
            }
        }
    	stage('Cleaning up') {
        	steps {
            	sh "docker rmi $registry:develop"
        	}
		}
    }
}
