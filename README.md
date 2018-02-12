# ibm-java-apm-containers
Repo for testing/helping Datadog customer's get started with OpenJ9 Java APM using Containers

# Objectives
- Get a sample Java application, running in a container using [IBM's JVM (OpenJ9)](https://en.wikipedia.org/wiki/OpenJ9), running and reporting Java APM metrics to Datadog.

- Follow and adapt the [Datadog instructions](https://docs.datadoghq.com/tracing/languages/java/)
for container usage. See [Dockerfile](./Dockerfile).

# Prerequisites
- Install Java (Either OpenJ9 or your favorite flavor (OpenJDK, Oracle, etc)) for your platform

# Getting Started
- Install [Gradle](https://gradle.org/) (on OSX: `brew install gradle`)
- (One time only) Run `gradle wrapper` to generate gradle wrapper (complete)
- Run `./gradlew build`
- Run `java -jar build/libs/gs-spring-boot-docker-0.1.0.jar` to test outside of Docker (hit `http://localhost:8080/`)
- Run `docker build -t helloworld .`
