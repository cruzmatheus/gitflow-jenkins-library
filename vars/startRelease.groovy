import com.github.cruzmatheus.commands.GitFlowCommands

def pom
def olderVersion
def newVersion

def call(Map params) {
    node {
        stage ("Checkout") {
            checkout scm
            pom = readMavenPom()
        }
        stage("Build") {
            sh("mvn clean verify")
        }
        stage("Starting release") {
            if (BRANCH_NAME == "develop") {
                sh(GitFlowCommands.startReleaseCommand(pom.version).trim())
            }
        }
        stage("Removing SNAPSHOT suffix") {
            if (BRANCH_NAME == "develop") {
                olderVersion = pom.version.toString()
                newVersion = olderVersion.replace("-SNAPSHOT", "")
                writeMavenPom model: pom
            }
        }
        stage("Commit new pom's release version") {
            if (BRANCH_NAME == "develop") {
                sh(GitFlowCommands.commitNewPomVersionCommand(olderVersion, newVersion).trim())
                sh(GitFlowCommands.pushCommand(GitFlowCommands.getReleaseBranchName(newVersion)))
            }
        }
        stage("Incrementing pom's version") {
            if (BRANCH_NAME == "develop") {
                sh(GitFlowCommands.switchToDevelopBranchCommand())
                def version = newVersion.split("\\.")
                version[-1] = version[-1].toInteger() + 1
                pom.version = newVersion = version.join('.').concat("-SNAPSHOT")
                writeMavenPom model: pom
                sh(GitFlowCommands.commitNewPomVersionCommand(olderVersion, newVersion).trim())
                sh(GitFlowCommands.pushCommand("develop"))

            }
        }
    }
}