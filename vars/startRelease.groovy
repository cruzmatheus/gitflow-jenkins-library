import com.github.cruzmatheus.commands.GitFlowCommands

com.github.cruzmatheus.commands.GitFlowCommands

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
                sh(GitFlowCommands.START_RELEASE_COMMAND)
            }
        }
    }
}