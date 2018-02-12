FROM ibmjava:jre

# copy over our app
WORKDIR /app
COPY . /app

# get the Datadog java agent
wget -O dd-java-agent.jar 'https://search.maven.org/remote_content?g=com.datadoghq&a=dd-java-agent&v=LATEST'

EXPOSE 80
CMD ["java", ""]
