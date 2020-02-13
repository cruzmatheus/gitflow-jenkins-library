import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import com.github.cruzmatheus.commands.GitFlowCommands

class StartReleaseSpec extends JenkinsPipelineSpecification {
    def startReleasePipeline = null

    def setup() {
        startReleasePipeline = loadPipelineScriptForTest("vars/startRelease.groovy")
        startReleasePipeline.getBinding().setVariable( "scm", null )
        explicitlyMockPipelineStep("readMavenPom")() >> [version: "1.0.0-SNAPSHOT"]
        explicitlyMockPipelineStep("writeMavenPom")
    }

    def "should not start release when branch is not develop"() {
        given:
        startReleasePipeline.getBinding().setVariable( "BRANCH_NAME", "master" )

        when:
        startReleasePipeline()

        then:
        1 * getPipelineMock("sh")("mvn clean verify")
        0 * getPipelineMock("sh")(GitFlowCommands.START_RELEASE_COMMAND)
    }

    def "should start release when branch is develop"() {
        given:
        startReleasePipeline.getBinding().setVariable( "BRANCH_NAME", "develop" )

        when:
        startReleasePipeline()

        then:
//        1 * getPipelineMock("sh")("mvn clean verify")
        1 * getPipelineMock("sh")("git checkout -b release/1.0.0")
        1 * getPipelineMock("sh")("git commit -am \"Changing version from 1.0.0-SNAPSHOT to 1.0.0\"")
        1 * getPipelineMock("sh")("git push origin release/1.0.0")
        1 * getPipelineMock("sh")("git checkout develop")
        1 * getPipelineMock("sh")("git commit -am \"Changing version from 1.0.0-SNAPSHOT to 1.0.1-SNAPSHOT\"")
        1 * getPipelineMock("sh")("git push origin develop")
    }
}