def call(Map params) {
    node {
        stage ("Checkout") {
            checkout scm
        }
        stage("Build") {
            sh("mvn clean install -X")
        }
    }
}