package org.daniel

class GitSCMCheckout implements Serializable {
  def steps

  GitSCMCheckout(steps) {
    this.steps = steps
  }

  def gitCheckout(repository) {
    steps.sh "echo Checkout from repository: ${repository}"
  }

}
