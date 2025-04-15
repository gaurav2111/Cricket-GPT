# Use official OpenJDK image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file (adjust the name if yours is different)
COPY build/libs/app.jar app.jar

# Expose the port (adjust if you're using something else)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]