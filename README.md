 jenkins-shared-library
Jenkins Shared Library in Groovy.

## Library
`@Library('jenkins-library')`

Mapping the `jenkins-library` library name from Jenkins Configure.

## Example

`getChangeString`
```groovy
@Library('jenkins-library') _

pipeline {
  agent any
  stages {
    stage ("demo") {
        steps {
            script {
              change = getChangeString(currentBuild.changeSets)
              echo change
            }
        }
    }
  }
}
```


`buildMkdocs`
```groovy
@Library('jenkins-library') _

pipeline {
    ...(skip)

    stage ("build mkdocs") {
      when {
        expression {
            fileExists("mkdocs.yml")
        }
      }
      steps {
        script {
          (buildId, buildArn, buildArtifactUrl, buildArtifactsLocation) = buildMkdocs(
              CODEBUILD_PROJECT_MKDOCS: "${env.CODEBUILD_PROJECT_MKDOCS}", REPO_NAME: "${env.REPO_NAME}",
              GIT_BRANCH: "${GIT_BRANCH}", MKDOCS_S3_BUCKET: "${env.MKDOCS_S3_BUCKET}")
        }
      }
      post {
        always {
          echo "buildId: ${buildId}"
          echo "buildArn: ${buildArn}"
        }
        success {
          echo "buildArtifactUrl: ${buildArtifactUrl}"
          echo "buildArtifactsLocation: ${buildArtifactsLocation}"
        }
        failure {
          echo "TODO: Sned a notification."
        }
      }
    } 

    ...(skip)
}

```
