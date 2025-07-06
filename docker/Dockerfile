# Use the official maven/openjdk as a parent image
FROM maven:3.8.4-openjdk-11

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . .

# Package the application for production environment
RUN mvn clean package -DskipTests

# Use an OpenJDK image to reduce the final container size
FROM openjdk:11-jre-slim

# Set the working directory in the JRE container
WORKDIR /app

# Copy the JAR file from the first stage
COPY --from=0 /app/target/task-management-api-1.0.0.jar /app/task-manager.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/task-manager.jar"]
