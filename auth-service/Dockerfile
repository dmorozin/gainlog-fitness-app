# === BUILD STAGE ===
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy entire project
COPY . .

# Build only the auth-service module (and its dependencies)
RUN mvn -pl auth-service -am clean package -DskipTests

# === RUNTIME STAGE ===
FROM eclipse-temurin:21-jdk-jammy AS runner
WORKDIR /app

COPY --from=builder /app/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]