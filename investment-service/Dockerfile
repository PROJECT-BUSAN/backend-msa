FROM openjdk:11

#COPY . /home/spring

#WORKDIR /home/spring

ARG JAR_FILE=build/libs/investment-service.jar

COPY ${JAR_FILE} /HOME/spring/app.jar

WORKDIR /HOME/spring/

EXPOSE 8080
