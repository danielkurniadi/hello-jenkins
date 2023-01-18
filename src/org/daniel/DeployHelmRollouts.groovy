package org.daniel

import hudson.model.ChoiceParameterDefinition

class DeployHelmRollouts implements Serializable {
  def steps

  DeployHelmRollouts(steps) {
    this.steps = steps
  }

  def deployHelmRollouts() {
    steps.sh "echo this is deploy helm rollouts stage stage"
  }

}
