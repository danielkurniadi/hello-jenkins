/**
 * This pipeline describes a k8s container job, running bazel image build+push
 */

def environment  = env.ENVIRONMENT
def version      = env.VERSION

pipeline {
    agent {
        /**
        * For the sake of convenience, you can replace this agent with any
        * Ubuntu 20.04 agent of your preference.
        */
        kubernetes {
            yamlFile "old_pipeline/pod-template.yaml"
            label "jenkins-agent-daniel-build"
            defaultContainer "daniel-build"
            idleMinutes 180
        }
    }
    options {
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
    }
    stages {
        stage("Load parameters") {
            /**
            * Regarding this Jenkins parameters for multi-branch pipeline.
            *
            * I'm not sure if its a better practice to define this properties
            * inside the stage block like:
            *   1. https://devopscube.com/declarative-pipeline-parameters/
            *
            * Or outside the stage block like:
            *   2. https://stackoverflow.com/questions/35370810/
            */
            steps {
                script {
                    properties([
                        parameters([
                            [
                                $class: 'ChoiceParameter',
                                choiceType: 'PT_CHECKBOX',
                                name: 'services',
                                script: [
                                    $class: 'GroovyScript',
                                    script: [
                                        sandbox: true,
                                        script: '''return [
                                            "admin-service",
                                            "user-service",
                                            "product-service",
                                        ]'''.stripIndent()
                                    ]
                                ]
                            ],
                        ])
                    ])
                }
            }
        }
        stage("Setup pipeline vars") {
            steps {
                scripts {
                    currentUser = currentBuild.getBuildCauses()[0].userId // supplied by Jenkins
                    services = params.services
                    imageTag = "${GIT_COMMIT[0..7]}" // first-8 commit sha

                    if (services.size() < 1) {
                        error("ERROR: services must be a comma-delimited list of services to build")
                    }

                }
            }
        }
        stage("Slack chat notification") {
            /**
            * TODO: maybe we can use this plugin:
            * https://www.jenkins.io/doc/pipeline/steps/slack/
            */
            echo "TODO: Please hook a Slack notification to tell devs that we are building ${servicesToBuild}! Skipping ..."
        }
        stage("Approval") {
            input(
                message: "Pipeline waiting for authorised approval. Deploy to ${env.ENVIRONMENT}?",
                ok: "Proceed",
                submitter: "approver1@mycompany.com,approver2@mycompany.com"
            )
        }
        stage("Unit Tests") {
            steps {
                /** TODO@hardik: also help see if this is the right way to perform
                *   parallel steps. I tried to use matrix but it doesn't support
                *   dynamic axis. I prefer to do it this way if possible.
                */
                script {
                    // Construct unit test jobs to run.
                    unitTestJobs = [:]
                    services.each { service ->
                        unitTestJobs["unittest-${service}"] = {
                            node {
                                stage("UnitTest ${service}") {
                                    // MOCK: this pipeline is mocked
                                    sh "echo running Unit Test for ${service}"
                                }
                            }
                        }
                    }
                    // Run unit tests parallel
                    parallel unitTestJobs
                }
            }
        }
        stage("Build") {
            steps {
                /** TODO@hardik: also help see if this is the right way to perform
                *   parallel steps. I tried to use matrix but it doesn't support
                *   dynamic axis. I prefer to do it this way if possible.
                */
                script {
                    // Construct unit test jobs to run.
                    buildJobs = [:]
                    services.each { service ->
                        buildJobs["build-${service}"] = {
                            node {
                                stage("Build ${service}") {
                                    // MOCK: this pipeline is mocked
                                    sh "echo running Build for ${service}"
                                }
                            }
                        }
                    }
                    // Run unit tests parallel
                    parallel buildJobs
                }
            }
        }
    }
}
