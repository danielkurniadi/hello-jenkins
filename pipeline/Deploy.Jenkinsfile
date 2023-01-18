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
            yamlFile "old_pipeline/pod-template-build.yaml"
            label "jenkins-agent-dataworks-build"
            defaultContainer "dataworks-build"
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
        stage("Deploy Config Files") {
            steps {
                script {
                    // Construct unit test jobs to run.
                    deployConfigJobs = [:]
                    services.each { service ->
                        deployConfigJobs["deploy-config-${service}"] = {
                            node {
                                stage("Deploy Config ${service}") {
                                    // MOCK: this pipeline is mocked
                                    sh "echo running config files upgrade for ${service}"
                                }
                            }
                        }
                    }
                    // Run unit tests parallel
                    parallel deployConfigJobs
                }
            }
        }
        stage("Deploy Services") {
            steps {
                script {
                    // Construct unit test jobs to run.
                    deployServiceJobs = [:]
                    services.each { service ->
                        deployServiceJobs["deploy-svc-${service}"] = {
                            node {
                                stage("Deploy Service ${service}") {
                                    // MOCK: this pipeline is mocked
                                    sh "echo running Helm rollouts for ${service}"
                                }
                            }
                        }
                    }
                    // Run unit tests parallel
                    parallel deployServiceJobs
                }
            }
        }
        stage("Custom Build Summary") {
            /**
            * TODO: custom build information of what services that we are deploying.
            */
            echo "TODO: Please summarize that we are deploying ${servicesToBuild} to display this in Jenkins dashboard"
        }
    }
}
