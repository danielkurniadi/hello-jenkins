package org.daniel

class GitSCMCheckout implements Serializable {
  def steps

  GitSCMCheckout(steps) {
    this.steps = steps
  }

  def gitCheckout(repository) {
    steps.bat "echo Checkout from repository: ${repository}"
  }

}
