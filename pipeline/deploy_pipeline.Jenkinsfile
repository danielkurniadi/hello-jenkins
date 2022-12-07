@Library('composableLib')
import org.daniel.GitSCMCheckout
import org.daniel.LoadParameters
import org.daniel.SlackNotification
import org.daniel.CustomBuildSummary
import org.daniel.ApprovalInput
import org.daniel.DeploySecrets
import org.daniel.DeployHelmRollouts

pipeline {
    agent any
    stages {
        stage ('GitSCMCheckout') {
            steps {
                script {
                    def scmCheckout = new GitSCMCheckout(this)
                    scmCheckout.gitCheckout('test Repo')
                }
            }
            
        }
        
        stage ('Load Parameters') {
            steps {
                script {
                    def loadParameters = new LoadParameters(this)
                    loadParameters.load('x parameter')
                }
            }
        }
        
        stage ('SlackNotification') {
            steps {
                script {
                    def slackNotification = new SlackNotification(this)
                    slackNotification.sendSlackNotification("This is my notification")
                }
            }
        }
        
        stage ('Approval Input') {
            steps {
                script {
                    def userInput = new ApprovalInput(this)
                    userInput.takeInput()
                }
            }
        }
        
        stage('Deploy Secrets') {
            steps {
                script {
                    def deploy = new DeploySecrets(this)
                    deploy.deploySecrets()
                }
            }
        }
        
        stage ('Deploy Helm Rollouts') {
            steps {
                script {
                    def deployHelm = new DeployHelmRollouts(this)
                    deployHelm.deployHelmRollouts()
                }
            }
        }
        
        stage ('Build Summary') {
            steps {
                script {
                    def customBuildSummary = new CustomBuildSummary(this)
                    customBuildSummary.buildSummary()
                }
            }
        }
    }
}
