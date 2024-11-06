# Use a base image with OpenJDK 11
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven build output (jar file) into the container
COPY target/devopsSpring_5DS3_G1-1.0.jar /app/devopsSpring_5DS3_G1.jar

# Expose the port on which the app will run
EXPOSE 8082

# Command to run the app
ENTRYPOINT ["java", "-jar", "devopsSpring_5DS3_G1.jar"]
