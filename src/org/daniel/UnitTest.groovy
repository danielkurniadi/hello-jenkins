package org.daniel

import hudson.model.ChoiceParameterDefinition

class UnitTest implements Serializable {
  def steps

  UnitTest(steps) {
    this.steps = steps
  }

  def runTests() {
    steps.bat "echo this is unit test stage"
  }

}
