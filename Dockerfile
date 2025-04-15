# Stage 1: Build the app
FROM gradle:7.6.0-jdk17 AS builder
COPY . /home/app
WORKDIR /home/app
RUN gradle bootJar

# Stage 2: Run the app
FROM eclipse-temurin:17-jdk
COPY --from=builder /home/app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
