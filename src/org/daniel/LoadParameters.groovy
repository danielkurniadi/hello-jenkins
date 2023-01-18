package org.daniel

import hudson.model.ChoiceParameterDefinition

class LoadParameters implements Serializable {
  def steps

  LoadParameters(steps) {
    this.steps = steps
  }

  def load(param) {
    steps.sh "echo load parameters from jenkins: ${param}"
    steps.properties([steps.parameters([steps.string(defaultValue: 'test', description: 'test', name: 'test'), steps.choice(choices: ['choice1', 'choice2'], description: 'this is test choice parameter', name: 'ChoiceParam')])])
  }

}
