def call(Map params) {
    node {
        stage ("Checkout") {
            checkout scm
        }
        stage("Build") {
            sh("mvn clean verify")
        }
        stage("Starting release") {
            if (BRANCH_NAME == "develop") {
                sh("mvn gitflow:release-start")
            }
        }
    }
}