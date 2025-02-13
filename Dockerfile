
# Stage 1: Build the appl# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-23 AS build

# Set the working directory to the root of the project
WORKDIR /movies

# Copy pom.xml and source code
COPY movies/pom.xml .
COPY movies/src ./src

# Package the application, skipping tests
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:23-jdk-slim

# Set the working directory to the root of the project
WORKDIR /

# Copy the Maven build output (JAR file) into the container
COPY --from=build /movies/target/movies-0.0.1-SNAPSHOT.jar project.jar

# Expose the port your application runs on
# EXPOSE 8080

# Command to run the application:w
ENTRYPOINT ["java", "-jar", "project.jar", "--port=8080"]
