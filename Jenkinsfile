pipeline {
  agent any
  tools {
    maven 'M3'
  }

  stages {

    stage('Checkout Application Git Branch') {
        steps {
            git url: 'https://github.com/xogur1212/droneService.git',
                branch: 'main'
        }
        post {
                failure {
                  echo 'Repository clone failure !'
                }
                success {
                  echo 'Repository clone success !'
                }
        }
    }
  }

}

