# Step 1: Use an official Gradle image as a build environment
FROM gradle:8.1.1-jdk17 AS build

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY gradlew .
COPY gradle /app/gradle
COPY build.gradle settings.gradle /app/

# Copy the source code
COPY src /app/src

# Grant execution permissions to the Gradle wrapper
RUN chmod +x gradlew

# Build the application, excluding tests
RUN ./gradlew clean build --exclude-task test

# Step 2: Use a slim OpenJDK image for runtime
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
