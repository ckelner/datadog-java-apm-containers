# Datadog Java APM Containers
Repo for Datadog customers to get started with Java APM using Containers.

***Note: This is not meant to be a distributed tracing example, but simply a small helpful project to get started with running with Datadog Java APM in a containerized environment. Additionally it tests the OpenJ9 JVM w/ the Datadog Java APM. It should not be used as a production-grade reference of any kind.***

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**
- [Objectives](#objectives)
- [Noteworthy](#noteworthy)
- [TODO](#todo)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Send APM Metrics to Datadog (OpenJDK)](#send-apm-metrics-to-datadog-openjdk)
  - [Run the Datadog Dockerized Agent](#run-the-datadog-dockerized-agent)
  - [Run the Java example](#run-the-java-example)
  - [Additional Docker commands](#additional-docker-commands)
  - [See APM in Datadog](#see-apm-in-datadog)
- [Send APM Metrics to Datadog (OpenJ9)](#send-apm-metrics-to-datadog-openj9)
  - [Run the Java example](#run-the-java-example-1)
  - [Issues with OpenJ9](#issues-with-openj9)
  - [See APM in Datadog](#see-apm-in-datadog-1)
- [Hacks](#hacks)
- [Notes on Java APM Support](#notes-on-java-apm-support)
- [Resources](#resources)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Objectives
Get a sample Java application, running in a container using and reporting Java APM metrics to Datadog via the Dockerized Datadog Agent.

# Noteworthy
- This small project is for demonstration purposes only.
- It does not make use any container orchestrator.
- It's original intent was to be used on a developer's local machine.

# TODO
- Test on AWS EC2
- Resolve [issues for OpenJ9](#issues-with-openj9)
- Fix [hacks](#hacks)

# Project Structure
- [agent](./agent): Contains a [dockerfile](./agent/Dockerfile) and [datadog.conf](./agent/datadog.conf) for building the containerized Datadog agent locally w/ some minimal configuration.
- [datadog](./datadog): Contains the `dd-java-agent.jar` version `0.3.0` as of 2018/02/12. Ideally this gets pulled in at build time and incorporated into the gradle build process.
- [gradle](./gradle): for gradle wrapper (run across multiple platforms)
- [src](./src/main/java/hello): Contains our very simple Sprint Boot application code.
- [.gitignore](./.gitignore): For ignoring files and directories generated by build tools
- [build.gradle](./build.gralde): Gradle build definition
- [Dockerfile](./Dockerfile): Primary Dockerfile for our simple Java app using OpenJDK
    - Copies over our WAR, exposes 8080 and sets several necessary Datadog Java APM properties
- [Dockerfile-openj9](./Dockerfile-openj9): A test bed for working with OpenJ9.
    - Uses `ibmjava:jre` - e.g. IBM Java/OpenJ9 JVM.
- [gradlew](./gradlew) and [gradlew.bat](./gradlew.bat): Gradle wrapper files - used for building Java project.

# Prerequisites
- Install Java; OpenJDK, Oracle, etc for your platform
- Install Docker for your platform

# Send APM Metrics to Datadog (OpenJDK)
## Run the Datadog Dockerized Agent
- Run to build the image: `docker build -t dd-agent ./agent/`
- Run the following, replacing `{your_api_key_here}` with your own DD API key.
  ```
  docker run -d --rm --name dd-agent \
    -v /var/run/docker.sock:/var/run/docker.sock:ro \
    -v /proc/:/host/proc/:ro \
    -v /sys/fs/cgroup/:/host/sys/fs/cgroup:ro \
    -e API_KEY={your_api_key_here} \
    -e DD_APM_ENABLED=true \
    -p 8126:8126/tcp \
    -e SD_BACKEND=docker \
    -e LOG_LEVEL=DEBUG \
    -e DD_LOGS_STDOUT=yes \
    -e DD_PROCESS_AGENT_ENABLED=true \
    dd-agent
  ```

## Run the Java example
- Run `./gradlew build` (or `gradlew.bat` if on windows)
- Run to build the docker image: ```DD_AGENT_IP_ADDR=`docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' dd-agent` docker build -t dd-java-apm --build-arg DD_AGENT_IP=$DD_AGENT_IP_ADDR .```
- Run to start the container:
  ```
  docker run -d -p 8081:8080 --rm --name dd-java-apm dd-java-apm \
  -e TAGS=host:dd-java-apm-demo-openjdk,env:demo
  ```

## Additional Docker commands
- Run to see the containers running: `docker ps`
- Run to see container logs: `docker logs dd-java-apm`
- Run to get to bash prompt for the container: `docker exec -it dd-java-apm /bin/bash`
- Run to stop the container: `docker stop dd-java-apm`
- Run to remove the container: `docker rm dd-java-apm`

## See APM in Datadog
- Hit these web urls locally to generate some APM metrics and traces:
    - http://localhost:8081
    - http://localhost:8081/slow
    - http://localhost:8081/sleepy
- Visit [Datadog APM env:demo](https://app.datadoghq.com/apm/services?env=demo) and the `dd-java-apm-example-openjdk` service should be listed.

# Send APM Metrics to Datadog (OpenJ9)
- Run the [Datadog Dockerized Agent as described in the OpenJ9 Section](#run-the-datadog-dockerized-agent)

## Run the Java example
- Run `./gradlew build` (or `gradlew.bat` if on windows)
- Run to build the docker image: ```DD_AGENT_IP_ADDR=`docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' dd-agent` docker build -t dd-java-apm-j9 --build-arg DD_AGENT_IP=$DD_AGENT_IP_ADDR -f Dockerfile-openj9 .```
- Run to start the container:
  ```
  docker run -d -p 8080:8080 --rm --name dd-java-apm-j9 dd-java-apm-j9 \
  -e TAGS=host:dd-java-apm-demo-openj9,env:demo
  ```
- See [Additional Docker commands](#additional-docker-commands) above for more docker CLI cmds.

## Issues with OpenJ9
These may be due to user error, and therefore may not be valid issues with OpenJ9. These are being reviewed for validity.

1. When using the OpenJ9 JRE all resources (`/`, `/slow`, & `/sleepy`) are **NOT** reported.
To reproduce, follow the instructions in the section [Send APM Metrics to Datadog (OpenJ9)](#send-apm-metrics-to-datadog-openj9) to get the Datadog agent and Java container running. Then hit the following urls several times over the course of a few minutes:
    1. http://localhost:8080/
    1. http://localhost:8080/slow
    1. http://localhost:8080/sleepy
What you should observe under OpenJ9 is that you'll only see one resource: https://cl.ly/1X0c131b0W0e.
However if you examine the trace metrics themselves it appears that they do
exist: https://cl.ly/1c0G2j3h2Q0Z.
If you repeat these steps under OpenJDK (follow the [Send APM Metrics to Datadog (OpenJDK)](send-apm-metrics-to-datadog-openjdk) section) each of these resources will report as seen here: https://cl.ly/1i3N151Y3g0e.
1. [This error shows up in the logs](https://gist.github.com/ckelner/ffbd6182bdff27929715a0d85ac991b4) even with [Application.java](https://github.com/ckelner/OpenJ9-jvm-datadog-apm-containers/blob/5d00c7d6fbdb440ee1171d07442afa5d40533dd7/src/main/java/hello/Application.java) in its most simple form (revision `5d00c7d6fbdb440ee1171d07442afa5d40533dd7`); This may or may not be related to the issue above.
1. Under OpenJ9, the `@Trace` annotation was required with Spring Boot - under OpenJDK this is not required (simply comment out the `@Trace` annotations and build the jar and run the docker container to verify).

## See APM in Datadog
- Hit these web urls locally to generate some APM metrics and traces (be sure to [note the issues with OpenJ9](#issues-with-openj9)):
    - http://localhost:8080
    - http://localhost:8080/slow
    - http://localhost:8080/sleepy
- Visit [Datadog APM env:demo](https://app.datadoghq.com/apm/services?env=demo) and the `dd-java-apm-example-openj9` service should be listed.

# Hacks
- The `dd-java-agent.jar` is stored in this repo at version `0.3.0` - it may need to be updated, to do so run `wget -O datadog/dd-java-agent.jar 'https://search.maven.org/remote_content?g=com.datadoghq&a=dd-java-agent&v=LATEST'` and place the jar in the [./datadog](./datadog) directory.
    - TODO: This should probably get covered as part of the gradle build process

# Notes on Java APM Support
As of 2018/02/12 Java APM has out of the box support for many popular Java frameworks, app servers, and data stores - check the [Datadog APM docs for the up to date list](https://docs.datadoghq.com/tracing/languages/java/#integrations).
- Java Servlet Compatible - Many application servers are Servlet compatible, such as Tomcat, Jetty, Websphere, Weblogic, etc. Also, frameworks like Spring Boot and Dropwizard inherently work because they use a Servlet compatible embedded application server.
- OkHTTP | 3.x
- Apache HTTP Client | 4.3 +
- JMS 2 | 2.x
- JDBC | 4.x
- MongoDB | 3.x
- Cassandra | 3.2.x

# Resources
- https://github.com/DataDog/docker-dd-agent
- https://docs.datadoghq.com/tracing/languages/java/#setup
- https://docs.datadoghq.com/api/?lang=bash#tracing
