package com.github.cruzmatheus.commands

class GitFlowCommands {
    public static final START_RELEASE_COMMAND = "mvn gitflow:release-start --batch-mode -DpushRemote=true"

    static String startReleaseCommand(pomVersion) {
        return "git checkout -b release/${pomVersion}"
    }

    static String changePomVersionCommand(pomVersion) {
        return "mvn versions:set -DgenerateBackupPoms=false -DnewVersion=${pomVersion}"
    }

    static String commitNewPomVersionCommand(olderVersion, newVersion) {
        return "git commit -am \"Changing version from ${olderVersion} to ${newVersion}\""
    }

    static String switchToDevelopBranchCommand() {
        return "git checkout develop"
    }

    static String pushCommand(branchName) {
        return "git push origin ${branchName}"
    }

    static String getReleaseBranchName(branchName) {
        return "release/${branchName}"
    }

}
