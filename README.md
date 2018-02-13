# OpenJ9 JVM Datadog APM Containers
Repo for testing/helping Datadog customer's get started with OpenJ9 Java APM using Containers.

***Note: THIS IS NOT MEANT TO BE A DISTRIBUTED TRACING EXAMPLE, SIMPLY HOW TO GET STARTED WITH DATADOG APM WITH THE OpenJ9 JVM AND DOCKER***

# Objectives
- Get a sample Java application, running in a container using [IBM & Eclipse's JVM Distribution (OpenJ9)](https://en.wikipedia.org/wiki/OpenJ9), running and reporting Java APM metrics to Datadog via the Docker Datadog Agent.
- Follow and adapt the [Datadog Java APM instructions](https://docs.datadoghq.com/tracing/languages/java/)
for container usage.

# Noteworthy
- This small project is for demonstration purposes only.
- It does not make use any container orchestrator.
- It's original intent was to be used on a developer's local machine.

# Project Structure
-

# Prerequisites
- Install Java (Either OpenJ9 or your favorite flavor (OpenJDK, Oracle, etc)) for your platform
- Install Docker for your platform

# Send APM Metrics to Datadog
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
- Install [Gradle](https://gradle.org/) (on OSX: `brew install gradle`)
- (One time only) Run `gradle wrapper` to generate gradle wrapper (complete)
- Run `./gradlew build`
- Run `java -jar build/libs/gs-spring-boot-docker-0.1.0.jar` to test outside of Docker (hit `http://localhost:8080/`)
- Run `wget -O datadog/dd-java-agent.jar 'https://search.maven.org/remote_content?g=com.datadoghq&a=dd-java-agent&v=LATEST'`
  - TODO: This should probably get covered as part of the gradle build process
- Run to build the image: `docker build -t dd-java-apm-hello-world .`
- Run to start the container:
```
docker run -d -p 8080:8080 --rm --name dd-java-apm dd-java-apm-hello-world \
-e TAGS=host:dd-java-apm-demo,env:demo
```
- Run to see the containers running: `docker ps`
- Run to see container logs: `docker logs dd-java-apm`
- Run to get to bash prompt for the container: `docker exec -it dd-java-apm /bin/bash`
- Run to stop the container: `docker stop dd-java-apm`
- Run to remove the container: `docker rm dd-java-apm`

# Resources
- https://github.com/DataDog/docker-dd-agent
- https://docs.datadoghq.com/tracing/languages/java/#setup
- https://docs.datadoghq.com/api/?lang=bash#tracing

# Java APM
As of 2018/02/12 Java APM has out of the box support for many popular Java frameworks, app servers, and data stores - check the [Datadog APM docs for the up to date list](https://docs.datadoghq.com/tracing/languages/java/#integrations).
- Java Servlet Compatible - Many application servers are Servlet compatible, such as Tomcat, Jetty, Websphere, Weblogic, etc. Also, frameworks like Spring Boot and Dropwizard inherently work because they use a Servlet compatible embedded application server.
- OkHTTP | 3.x
- Apache HTTP Client | 4.3 +
- JMS 2 | 2.x
- JDBC | 4.x
- MongoDB | 3.x
- Cassandra | 3.2.x
