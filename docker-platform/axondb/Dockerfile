FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8023 8123
RUN apk add --update bash
ADD axondb-server.jar app.jar
ADD axondb.properties .
ENTRYPOINT ["java", "-jar", "app.jar"]
