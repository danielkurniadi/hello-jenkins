package org.dataworks

import hudson.model.ChoiceParameterDefinition

class BuildPushImage implements Serializable {
  def steps

  BuildPushImage(steps) {
    this.steps = steps
  }

  def buildPush() {
    steps.bat "echo this is build push stage"
  }

}
