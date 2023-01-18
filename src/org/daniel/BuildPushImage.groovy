package org.daniel

import hudson.model.ChoiceParameterDefinition

class BuildPushImage implements Serializable {
  def steps

  BuildPushImage(steps) {
    this.steps = steps
  }

  def buildPush() {
    steps.sh "echo this is build push stage"
  }

}
