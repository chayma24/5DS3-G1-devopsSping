#FROM openjdk:17-jdk-slim

#EXPOSE 8082

#COPY target/*.jar app.jar

#ENTRYPOINT ["java", "-jar", "/app.jar"]

# Use a specific version of Alpine-based JRE for better reproducibility
FROM adoptopenjdk/openjdk11:jre-11.0.9_11.1-alpine@sha256:58254737530df7f7e7acfe3e7538a027f898ac05accd2e15adcef34d5a33ae72

# Add labels for better maintainability
LABEL maintainer="g3@ds3.tn" \
      version="3.0" \
      description="Java Spring Boot application (without a database server)"

# Set the working directory
WORKDIR /app

# Copy the jar file to the container
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# Install curl, add non-root user, set up directories, generate checksum, and set permissions
# Group commands to reduce layers
RUN apk add --no-cache curl && \
    addgroup -S appgroup && \
    adduser -S appuser -G appgroup && \
    chown -R appuser:appgroup /app && \
    sha256sum /app/app.jar > /app/app.jar.sha256 && \
    chmod 400 /app/app.jar /app/app.jar.sha256

# Switch to non-root user
USER appuser

# Expose the port the app runs on
EXPOSE 8082

# Add a health check for the running container
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]


## Build the Docker image
#docker build -t chayma24/5ds3-g1-devopsspring:latest --build-arg JAR_FILE=target/5DS3-G1-devopsSpring-1.0.jar .

## Run the Docker container
#docker run -d -p 8082:8082 --name devops_spring_container chayma24/5ds3-g1-devopsspring:latest

## Test the application
#docker exec devops_spring_container curl -f http://localhost:8082/actuator/health
#docker exec -it devops_spring_container /bin/sh

#docker stop devops_spring_container
#docker rm devops_spring_container
