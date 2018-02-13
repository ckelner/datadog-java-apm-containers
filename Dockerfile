FROM ibmjava:jre

# copy over our app
WORKDIR /app
COPY build/libs/gs-spring-boot-docker-0.1.0.jar /app
# Might be hacky -- dunno
COPY datadog/dd-java-agent.jar /app

EXPOSE 8080
# theoretically we don't need dd.agent.host here - when we run docker we use
# `--link dd-agent:dd-agent` which should point out APM requests to the dd-agent container
ENTRYPOINT ["java", "-javaagent:/app/dd-java-agent.jar", \
"-Ddatadog.slf4j.simpleLogger.defaultLogLevel=debug", \
"-Ddd.service.name=dd-java-apm-example", \
"-jar", "/app/gs-spring-boot-docker-0.1.0.jar"]
