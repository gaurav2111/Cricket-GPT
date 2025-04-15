# ---- Stage 1: Build ----
FROM gradle:7.6.0-jdk17 AS builder

# Set working directory
WORKDIR /home/app

# Copy everything
COPY . .

# Build the application
RUN gradle bootJar

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jdk

# Copy the jar from the builder stage
COPY --from=builder /home/app/build/libs/*.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
