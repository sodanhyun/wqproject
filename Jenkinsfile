pipeline {
    agent any

    environment {
        timestamp = "${System.currentTimeMillis() / 1000L}"
    }

    stages {
        stage('Prepare') {
            steps {
                script {
                    // Get the ID of the sbb:latest image
                    def oldImageId = sh(script: "docker images be_wqproject:latest -q", returnStdout: true).trim()
                    env.oldImageId = oldImageId
                }

                git branch: 'main',
                    url: 'https://github.com/sodanhyun/wqproject-be'
            }

            post {
                success {
                    sh 'echo "Successfully Cloned Repository"'
                }
                failure {
                    sh 'echo "Fail Cloned Repository"'
                }
            }
        }

        stage('Build Gradle') {
            steps {
                dir('.') {
                    sh """
                    cp /application-prod.yml ./src/main/resources/application-prod.yml
                    """
                }
                dir('.') {
                    sh """
                    chmod +x gradlew
                    """
                }

                dir('.') {
                    sh """
                    ./gradlew clean bootjar
                    """
                }
            }

            post {
                success {
                    sh 'echo "Successfully Build Gradle Test"'
                }
                 failure {
                    sh 'echo "Fail Build Gradle Test"'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t be_wqproject:${timestamp} ."
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    // Check if the container is already running
                    def isRunning = sh(script: "docker ps -q -f name=be_wqproject", returnStdout: true).trim()

                    if (isRunning) {
                        sh "docker rm -f be_wqproject"
                    }

                    // Run the new container
                    try {
                        sh """
                        docker run \
                          --name=be_wqproject \
                          -p 8080:8080 \
                          -v /home/docker_projects/wqproejct-be/volumes/gen:/gen \
                          --restart unless-stopped \
                          --network application \
                          -e TZ=Asia/Seoul \
                          -d \
                          be_wqproject:${timestamp}
                        """
                    } catch (Exception e) {
                        // If the container failed to run, remove it and the image
                        isRunning = sh(script: "docker ps -q -f name=be_wqproject", returnStdout: true).trim()

                        if (isRunning) {
                            sh "docker rm -f be_wqproject"
                        }

                        def imageExists = sh(script: "docker images -q be_wqproject:${timestamp}", returnStdout: true).trim()

                        if (imageExists) {
                            sh "docker rmi be_wqproject:${timestamp}"
                        }

                        error("Failed to run the Docker container.")
                    }

                    // If there's an existing 'latest' image, remove it
                    def latestExists = sh(script: "docker images -q be_wqproject:latest", returnStdout: true).trim()

                    if (latestExists) {
                        sh "docker rmi be_wqproject:latest"

                        if(!oldImageId.isEmpty()) {
                        	sh "docker rmi ${oldImageId}"
                        }
                    }

                    // Tag the new image as 'latest'
                    sh "docker tag be_wqproject:${env.timestamp} be_wqproject:latest"
                }
            }
        }
    }
}