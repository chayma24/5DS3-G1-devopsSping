pipeline {
    agent any

    stages {
        stage('Checkout GIT') {
            steps {

                echo 'Pulling... '
                checkout scm
            }
        }

        stage('Maven Clean') {
            steps {
               echo "Clean avec maven"
                    sh "mvn clean"

            }

        }

        stage('Maven Compile') {
            steps {
                echo "compilation avec maven"

                    sh "mvn compile"
                    sh "mvn package"

            }
        }

        /*

         stage('OWASP Dependency-Check') {
            steps {
                dir('tp-foyer') {
                    echo 'Exécution de l\'analyse de dépendances avec OWASP Dependency-Check'
                    sh 'mvn org.owasp:dependency-check-maven:check'
                }
            }
        }
        stage('Publish Dependency-Check Report') {
            steps {
                dir('tp-foyer') {
                    echo 'Publication du rapport OWASP Dependency-Check'
                     publishHTML(
                        target: [
                            reportName: 'OWASP Dependency-Check Report',
                            reportDir: 'target/dependency-check-report', // Le répertoire où le rapport est généré
                            reportFiles: 'index.html', // Le fichier HTML généré par OWASP Dependency-Check
                            keepAll: true
                       ]
                     )
                }
            }
        }*/

       /*  stage('MOCKITO Test') {
            steps {
                echo "test avec maven"
                dir('tp-foyer') {
                    sh "mvn test"
                }
            }
        } */

         /*stage('Docker Security Scanning with Trivy') {
            steps {
                dir("tp-foyer") {
                    script {
                            // Récupérer dynamiquement les IDs des conteneurs en cours d'exécution
                            def containerIds = sh(script: "docker ps -q", returnStdout: true).trim().split("\n")
                            def reportsDir = "trivy-reports/"
                            sh "mkdir -p ${reportsDir}"

                            // Exécuter un scan Trivy pour chaque conteneur en parallèle
                            parallel containerIds.collectEntries { containerId ->
                                ["Scan ${containerId}": {
                                    try {
                                        // Récupérer l'image associée à l'ID du conteneur
                                        def image = sh(script: "docker inspect --format '{{.Config.Image}}' ${containerId}", returnStdout: true).trim()
                                        echo "Scanning container image: ${image} with Trivy"

                                        // Exécuter le scan Trivy et sauvegarder le rapport en format JSON
                                        sh """
                                        trivy image \
                                        --severity LOW,MEDIUM,HIGH,CRITICAL \
                                        --exit-code 1 \
                                        --no-progress \
                                        --format json \
                                        --output ${reportsDir}/trivy-report-${containerId}.json \
                                        ${image}
                                        """

                                    } catch (Exception e) {
                                        echo "Failed to scan container ${containerId}: ${e}"
                                    }
                                }]
                            }

                    }
                }
            }
        }


         // Étape pour archiver les rapports de Trivy
         stage('Archive Reports') {
             steps {
                 dir("tp-foyer") {
                     archiveArtifacts artifacts: "trivy-reports/*.json", allowEmptyArchive: true
                 }
             }
         }*/



         stage('MVN Sonarqube') {
            steps {
                echo "analyse avec sonarqube"



                    withCredentials([string(credentialsId: '29b9b498-d43a-49f1-b495-08e952f9e60e',
                                        variable: 'SONAR_TOKEN')]) {
                        sh """
                        mvn sonar:sonar \
                        -Dsonar.host.url=$SONAR_HOST_URL \
                        -Dsonar.login=\$SONAR_TOKEN
                        """
                    }

            }
        }


       /*   stage('Nexus Deploy') {
            steps {
                echo "Déploiement sur Nexus"
                dir('tp-foyer') {
                    withCredentials([usernamePassword(credentialsId: 'bcc1b017-d8af-459d-883d-133048e255b8',
                                                   usernameVariable: 'NEXUS_USERNAME',
                                                   passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh """
                        mvn deploy \
                        -DskipTests \
                        -Dusername=\$NEXUS_USERNAME \
                        -Dpassword=\$NEXUS_PASSWORD
                        """
                    }
                }
            }
        } */



        /*stage('Building backend image') {
            steps {
                echo "creating backend docker image"
                dir('tp-foyer') {
                    sh "docker build -f Dockerfile -t $BACKEND_IMAGE ."
                }
            }
        }*/


       /*  stage('building frontend image') {
            steps {
                echo "creating frontend docker image"
                dir('tp-foyer-frontend') {
                     //sh 'npm install'
                     //sh 'npm run build --prod'
                     sh "docker build -f Dockerfile-angular -t $FRONTEND_IMAGE ."
               }
            }
        } */

        /*stage('Pushing image') {
            steps {
                echo "Push docker images"
                // Utilise withCredentials pour récupérer les credentials Docker Hub
                withCredentials([usernamePassword(credentialsId: '8b6e20fb-38d6-41ce-a2f5-7a32a513881c',
                                                  usernameVariable: 'DOCKER_USERNAME',
                                                  passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh "docker login -u \${DOCKER_USERNAME} -p \${DOCKER_PASSWORD}" // \$ permet de récupérer la valeur de la variable non lu par Jenkins mais par le shell
                    sh "docker push $BACKEND_IMAGE"  // "$" va permettre à Jenkins de récupérer la valeur de la variable BACKEND_IMAGE
                  //  sh "docker push $FRONTEND_IMAGE"

                }
            }
        }

        stage('Start Docker Composer') {
            steps {
                echo "starting docker composer"
                sh "docker compose down" //arrete le conteneur s'il est deja en cours d'execution

                 /*lance le conteneur en arriere plan pour permettre à jenkins
                de continuer la prochaine etape du pipeline sans attendrent que
                 ce service docker se termine et reconstruis les images déjà existantes
                 lorsqu'on a eu à effectuer des modifs dans le code source ou dans dockerfile //
                sh "docker compose up -d --build"
            }
        }*/
    }
}