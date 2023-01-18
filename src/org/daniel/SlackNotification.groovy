package org.daniel

class SlackNotification implements Serializable {
  def steps

  SlackNotification(steps) {
    this.steps = steps
  }

  def sendSlackNotification(message) {
    steps.bat "echo sending slack notification: ${message}"
  }
}
