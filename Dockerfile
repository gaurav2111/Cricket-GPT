# ---- Stage 1: Build ----
FROM gradle:7.6.0-jdk17 AS builder

WORKDIR /home/app

COPY . .

# Clean and build while refreshing dependencies
RUN gradle bootJar --no-daemon --refresh-dependencies

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jdk

COPY --from=builder /home/app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
