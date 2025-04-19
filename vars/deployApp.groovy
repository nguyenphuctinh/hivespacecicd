def call(HiveSpaceProject project, String tag = "${env.BUILD_NUMBER}") {
    pipeline {
        agent any

        environment {
            GIT_REPO = project.gitRepo
            GIT_BRANCH = project.branch
        }

        stages {
            stage('Checkout') {
                steps {
                    git branch: GIT_BRANCH, url: GIT_REPO
                }
            }

            stage('Build & Push All Apps') {
                steps {
                    script {
                        withCredentials([usernamePassword(
                            credentialsId: project.credentialsId,
                            usernameVariable: 'DOCKER_USERNAME',
                            passwordVariable: 'DOCKER_PASSWORD'
                        )]) {
                            sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin"

                            project.apps.each { app ->
                                def imageTag = app.getFullImageTag(tag)
                                echo "🔧 Building image for ${app.name} at ${app.buildContext}"
                                sh "docker build -t ${imageTag} ${app.buildContext}"
                                sh "docker push ${imageTag}"
                            }
                        }
                    }
                }
            }
        }

        post {
            success {
                echo '✅ All images built and pushed successfully'
            }
        }
    }
}
