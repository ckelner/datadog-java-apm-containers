# ibm-java-apm-containers
Repo for testing/helping Datadog customer's get started with OpenJ9 Java APM using Containers.

_*Note: THIS IS NOT MEANT TO BE A DISTRIBUTED TRACING EXAMPLE, SIMPLY HOW TO GET STARTED WITH DATADOG APM WITH JAVA AND DOCKER*_

# Objectives
- Get a sample Java application, running in a container using [IBM's JVM (OpenJ9)](https://en.wikipedia.org/wiki/OpenJ9), running and reporting Java APM metrics to Datadog.

- Follow and adapt the [Datadog instructions](https://docs.datadoghq.com/tracing/languages/java/)
for container usage. See [Dockerfile](./Dockerfile).

# Prerequisites
- Install Java (Either OpenJ9 or your favorite flavor (OpenJDK, Oracle, etc)) for your platform
- Install Docker for your platform

# Getting Started
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
