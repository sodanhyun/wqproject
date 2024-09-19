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
                    def oldImageId = sh(script: "docker images BE_wqproject:latest -q", returnStdout: true).trim()
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
                    sh "docker build -t BE_wqproject:${timestamp} ."
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    // Check if the container is already running
                    def isRunning = sh(script: "docker ps -q -f name=BE_wqproject", returnStdout: true).trim()

                    if (isRunning) {
                        sh "docker rm -f BE_wqproject"
                    }

                    // Run the new container
                    try {
                        sh """
                        docker run \
                          --name=BE_wqproject \
                          -p 8080:8080 \
                          -v /docker_projects/wqproejct-be/volumes/gen:/gen \
                          --restart unless-stopped \
                          --network application \
                          -e TZ=Asia/Seoul \
                          -d \
                          BE_wqproject:${timestamp}
                        """
                    } catch (Exception e) {
                        // If the container failed to run, remove it and the image
                        isRunning = sh(script: "docker ps -q -f name=BE_wqproject", returnStdout: true).trim()

                        if (isRunning) {
                            sh "docker rm -f BE_wqproject"
                        }

                        def imageExists = sh(script: "docker images -q BE_wqproject:${timestamp}", returnStdout: true).trim()

                        if (imageExists) {
                            sh "docker rmi BE_wqproject:${timestamp}"
                        }

                        error("Failed to run the Docker container.")
                    }

                    // If there's an existing 'latest' image, remove it
                    def latestExists = sh(script: "docker images -q BE_wqproject:latest", returnStdout: true).trim()

                    if (latestExists) {
                        sh "docker rmi BE_wqproject:latest"

                        if(!oldImageId.isEmpty()) {
                        	sh "docker rmi ${oldImageId}"
                        }
                    }

                    // Tag the new image as 'latest'
                    sh "docker tag BE_wqproject:${env.timestamp} BE_wqproject:latest"
                }
            }
        }
    }
}