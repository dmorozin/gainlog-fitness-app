# === BUILD STAGE ===
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy entire project to allow parent/child POM resolution
COPY . .

# Build only the workout-service module
RUN mvn -pl workout-service -am clean package -DskipTests

# === RUNTIME STAGE ===
FROM eclipse-temurin:21-jdk-jammy AS runner
WORKDIR /app

# Copy only the JAR built in the previous stage
COPY --from=builder /app/workout-service/target/workout-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the correct port (make sure it's set in application.properties)
EXPOSE 8081

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
