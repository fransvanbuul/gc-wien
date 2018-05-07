FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8024 8124

# Apparently AxonHub needs Fonts
ENV LANG en_US.UTF-8
RUN apk add --update ttf-dejavu ttf-droid ttf-freefont ttf-liberation ttf-ubuntu-font-family && rm -rf /var/cache/apk/*

RUN apk add --update bash
ADD axonhub-server.jar app.jar
ADD axonhub.properties .
ENTRYPOINT ["java", "-jar", "app.jar"]
