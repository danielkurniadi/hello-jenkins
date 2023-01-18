package org.daniel

import hudson.model.ChoiceParameterDefinition

class ApprovalInput implements Serializable {
  def steps

  ApprovalInput(steps) {
    this.steps = steps
  }

  def takeInput() {
    steps.input message: 'Please decide whether pipeline should move forward or not', ok: 'Continue Build'
  }

}
