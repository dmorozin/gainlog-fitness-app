# === BUILD STAGE ===
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy entire project (parent + modules)
COPY .. .

# Build only api-gateway module
RUN mvn -pl api-gateway -am clean package -DskipTests

# === RUNTIME STAGE ===
FROM eclipse-temurin:21-jdk-jammy AS runner
WORKDIR /app

# Copy the built JAR
COPY --from=builder /app/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
