package org.daniel

import hudson.model.ChoiceParameterDefinition

class CustomBuildSummary implements Serializable {
  def steps

  CustomBuildSummary(steps) {
    this.steps = steps
  }

  def buildSummary() {
    steps.sh "echo this is build summary stage"
  }

}
