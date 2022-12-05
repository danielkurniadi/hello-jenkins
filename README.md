# Hello Jenkins project

## Background

Why and what is this for? What is the value of accomplishing this project?

As a background context, we previously have two Jenkins multi-branch pipelines that works fine for our CI and CD pipeline.
1. First pipeline builds our app image and push it to the cloud.
2. Second pipeline deploy our app image to our kubernetes cluster using helm rollout.

Now, we are switching to a declarative pipeline style and also looking into how we can make a more composable and clean pipeline codes written
in groovy script and Jenkinsfile.


The details of how the build and deploy script works is not important. If it makes your life easier for this task,
you can instead just replace the script with `echo "Hello World this is step: build image"`.

<br>


## Pipeline Diagram

As spoken, there are two pipelines: `Build` and `Deploy` pipeline. The following section describes the desired pipelines using diagram.

### Current (Old) Implementation

- `Build` pipeline: This pipeline is currently implemented in `old_pipeline/Build.Jenkinsfile`. When we create a multi-branch project in Jenkins, make sure to point the Jenkinfiles to path: `pipeline/Build.Jenkinsfile`.
- `Deploy` pipeline: This pipeline is currently implemented in `Deploy.Jenkinsfile`. When we create a multi-branch project in Jenkins, make sure to point the Jenkinfiles to path: `pipeline/Deploy.Jenkinsfile`.

### _Why refactoring_?

Both this pipelines require refactoring because everything is implemented in a single file and it feels like we can use groovy modules to refactor common functionalities of these two pipelines. Not to mention that, we also have different envs: `dev`, `staging`, and `production`.

Imagine scenarios where in `staging` and `production` we want to include step for approval gate from authorized operator while in `dev` env
we don't want to have the approval gate step i.e make it easy for developers to deploy their app without asking the authorized personel everytime they want to test out something in `dev`.

### `Build` pipeline

This pipeline is currently implemented in `old_pipeline/Build.Jenkinsfile`. It builds multiple services from a mono-repo multi-services (our developer wants to use monorepo style). Often, this pipeline is called fanout style where one pipeline has multiple builds / outputs.

Currently, we only have `Build.Jenkinsfile` for all environments but ideally, this is what the pipeline should looks like:

<br>

<p>
    <img src="docs/pipeline.build.png" alt>
    <em>Figure 1. Build (CI) pipeline for dev, staging and production environment </em>
</p>

### `Deploy` pipeline

This pipeline is currently implemented in `old_pipeline/Deploy.Jenkinsfile`. It deploy multiple services from a mono-repo multi-services (our developer wants to use monorepo style). Often, this pipeline is called fanout style where one pipeline has multiple deployments / outputs.

### Common Stages

There are several common stages between `Build` and `Deploy` pipeline.
This stage is color coded in blue circle in the above diagram.

- Load parameters: We put parameters that will prompt user input before the pipeline runs. See this [screenshots](). TODO: might should we move this to property instead of using stage?
- Slack notification: When a pipeline starts, we notify a slack channel that someone is starting this job.
- Approval input: We asks authorized personels (hardcoded by username) to approve the pipeline runs. If it timeouts or get rejected. The pipeline execution is dropped. We only use this in `dev` environment.

### Internal Stages

There are several internal stages in `Build` and `Deploy` pipeline.
This stage is color coded in yellow or orange circle in the above diagram.

> :bulb: 💡
>
> Don't worry about these steps. You may mock it with a simple echo script, for example:
> ```bash
> stage("Unit Tests") {
>   steps { sh "echo running tests ..." }
> }
> ```

<br>

<p>
    <img src="docs/pipeline.deploy.png" alt>
    <em>Figure 2. Deploy (CD) pipeline for dev, staging and production environment</em>
</p>

### Custom-build-summary Stage

Often, Jenkins build summary need to be enriched with what services we build and what are the parameters we took. Currently, it only display very limited information (see [screenshots](docs/pipeline.summary.png))

For example, we want to display:
1. What services the Jenkins user choses to build in this job.
2. What are other parameters the Jenkins user passes in the parameters stage.

I believe this specific needs can be catered directly using the following:

1. Using Jenkins plugin called [HTML Publisher](https://plugins.jenkins.io/htmlpublisher/). I haven't explored much but this is possibility?
2. Alterntively, we overwrite the current build display directly in the final stage.
    ```bash
    stage("Custom Build Summary") {
        script{
            currentBuild.description = "YOUR_BUILD_DESCRIPTION"
        }
    }
    ```

<br>

## Project Challenge

This section defines what help we need from you. Mainly regarding refactoring of the Jenkinsfile.

### 1. Refactoring for Composable Pipeline

What does composable means?

> Composable program can be assembled and composed from other smaller and modular programs.
> This smaller programs may have the same or different interfaces but it has an expected behavior such that the main program and be constructed by passing a list of smaller programs.
> A composable program is an old concept from Frontend development, often used when talking about React.js application but this concept should be applicable to any software engineering domain.

**Task** - Now, you are given two Jenkinsfiles for `Build` and `Deploy`. Refactor the Jenkinsfiles into composable stage / steps using groovy libraries / modules.

- The old pipeline scripts is found in `old_pipeline` folder
  * Build: [`old_pipeline/Build.Jenkinsfile`]().
  * Deploy: [`old_pipeline/Deploy.Jenkinsfile`]().
- The refactored `Build` and `Deploy` pipelines should be able to run in `development`, `staging`, and `production` cluster. See [Pipeline Diagram](#desired-pipeline-diagram).
- These refactored interfaces or classes in the groovy modules should be reuse-able for other pipelines.
- These `Build` and `Deploy` pipelines should be as modular and _compose-able_ as possible. Imagine if we can define each stage as an object that implements `StageRunner` interface. Then we gave a `List<StageRunner>` to define what steps should we run in the pipeline in sequence. This gives great extensibility in our Jenkins code.

In addition to this refactoring. We also need you to help with logging issue.

<br>

### 2. Research on logging to `kubectl logs`
In addition to this refactoring. We also need you to help with logging issue.

**Problem Statement**: Currently, I'm wondering if we can use SLF4J library in Jenkinfile or groovy class to log into a class.

**Task**:
- Research and see if we can just use SLF4J logger if possible. e.g.
    ```java
    @Slf4j
    class StageRunner {
        void run() {
            log.debug ‘Hello, World!’
        }
    }
    ```

- Problem and motivation for this is spoken in the online-video call on Monday 5th Dec 2022, 6pm India Time.
- Research and see if the SLF4J can redirect the log output to a file e.g. `/proc/1/fd/1`
- See: https://unix.stackexchange.com/questions/295883/whats-the-difference-between-1-and-proc-self-fd-1-redirection

