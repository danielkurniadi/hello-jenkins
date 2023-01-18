package org.daniel

import hudson.model.ChoiceParameterDefinition

class DeployHelmRollouts implements Serializable {
  def steps

  DeployHelmRollouts(steps) {
    this.steps = steps
  }

  def deployHelmRollouts() {
    steps.bat "echo this is deploy helm rollouts stage stage"
  }

}
