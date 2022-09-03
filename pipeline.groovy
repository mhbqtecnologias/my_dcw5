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
stage('Send image to Docker Hub') {
            steps{
                script {
                    docker.withRegistry( '', registryCredential) {
                        dockerImage.push()
                    }
                }
            }
        }
    	stage('Deploy') {
		    steps{
                step([$class: 'AWSCodeDeployPublisher',
                    applicationName: 'myappcodeploy',
                    awsAccessKey: "AKIATTYCTNZOH23WO4NN", // ALTERAR
                    awsSecretKey: "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", // ALTERAR
                    credentials: 'awsAccessKey',
                    deploymentGroupAppspec: false,
                    deploymentGroupName: 'groupcodeployapp', // ALTERAR
                    deploymentMethod: 'deploy',
                    excludes: '',
                    iamRoleArn: '',
                    includes: '**',
                    pollingFreqSec: 15,
                    pollingTimeoutSec: 600,
                    proxyHost: '',
                    proxyPort: 0,
                    region: 'us-east-1', // CHECAR
                    s3bucket: 'dcw5-s3bucket', // ALTERAR
                    s3prefix: '', 
                    subdirectory: '',
                    versionFileName: '',
                    waitForCompletion: true])
            }
        }
    	stage('Cleaning up') {
        	steps {
            	sh "docker rmi $registry:develop"
        	}
		}
    }
}