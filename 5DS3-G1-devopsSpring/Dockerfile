FROM openjdk:17-jdk-slim

EXPOSE 8082

ARG JAR_FILE
COPY target/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
