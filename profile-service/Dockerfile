FROM openjdk:11

ARG JAR_FILE=build/libs/profile-service.jar

COPY ${JAR_FILE} /HOME/spring/app.jar

WORKDIR /HOME/spring/

EXPOSE 8081
