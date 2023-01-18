package org.daniel

class SlackNotification implements Serializable {
  def steps

  SlackNotification(steps) {
    this.steps = steps
  }

  def sendSlackNotification(message) {
    steps.sh "echo sending slack notification: ${message}"
  }
}
