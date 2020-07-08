def call(Map buildParams) {    
    // Invoke CodeBuild
    codeBuild = awsCodeBuild(
        projectName: "${buildParams.CODEBUILD_PROJECT_MKDOCS}",
        credentialsType: 'keys',
        region: 'ap-northeast-1',
        sourceControlType: 'jenkins',
        envVariables: "[ {REPO_NAME, ${buildParams.REPO_NAME}} ]"
    )
    buildId = codeBuild.getBuildId()
    buildArn = codeBuild.getArn()
    buildArtifactsLocation = codeBuild.getArtifactsLocation()

    // TODO: Implement version tag in URL path.
    // Convert S3 ARN to URL
    buildArtifactUrl = buildArtifactsLocation.replace("arn:aws:s3:::", "s3://")
    switch (buildParams.GIT_BRANCH) {
    case 'develop':
        versionPath = 'dev'
        break
    case 'master':
        // Use root directory for latest release document.
        versionPath = ''
        break
    default:
        versionPath = 'youshouldntseethis'
    }
    // Declarative style didn't support S3 recursive cp. So use CLI directly.
    sh (
        script: "aws s3 cp ${buildArtifactUrl} \
            ${buildParams.MKDOCS_S3_BUCKET}/${buildParams.REPO_NAME}/${versionPath} \
            --recursive",
        returnStatus: true
    )

    return [buildId, buildArn, buildArtifactUrl, buildArtifactsLocation]

}