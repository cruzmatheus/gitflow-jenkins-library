import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

class StartReleaseSpec extends JenkinsPipelineSpecification {
    def startReleasePipeline = null

    def setup() {
        startReleasePipeline = loadPipelineScriptForTest("vars/startRelease.groovy")
        startReleasePipeline.getBinding().setVariable( "scm", null )
    }

    def "should start release when branch is develop"() {
        given:
        startReleasePipeline.getBinding().setVariable( "BRANCH_NAME", "develop" )

        when:
        startReleasePipeline()

        then:
        1 * getPipelineMock("sh")("mvn clean install -X")
    }
}