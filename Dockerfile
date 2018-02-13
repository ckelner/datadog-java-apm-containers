FROM ibmjava:jre

# copy over our app
WORKDIR /app
COPY build/libs/gs-spring-boot-docker-0.1.0.jar /app
# Might be hacky -- dunno
COPY datadog/dd-java-agent.jar /app

EXPOSE 8080

ENTRYPOINT ["java", "-javaagent:/app/dd-java-agent.jar", \
"-Ddd.service.name=dd-java-apm-example", \
"-Ddd.agent.host=172.17.0.2", \
"-jar", "/app/gs-spring-boot-docker-0.1.0.jar"]
