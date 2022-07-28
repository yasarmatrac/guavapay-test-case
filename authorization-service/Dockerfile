FROM adoptopenjdk/openjdk11:alpine-jre

# Refer to Maven build -> finalName
ARG JAR_FILE=build/libs/authorization-service-1.0-SNAPSHOT.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} authorization-service.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-Dspring.output.ansi.enabled=always", "-Duser.timezone=UTC","-jar","authorization-service.jar"]