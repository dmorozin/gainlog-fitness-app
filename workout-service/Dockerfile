# === BASE BUILD STAGE (common) ===
FROM maven:3.9.9-eclipse-temurin-21 AS base
WORKDIR /app

# Copy Maven descriptors first for dependency caching
COPY pom.xml .
COPY workout-service/pom.xml workout-service/

# Download dependencies (cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy full source
COPY . .

# === TEST STAGE ===
FROM base AS test
WORKDIR /app
# Run tests for this module (includes deps from base)
RUN mvn -pl workout-service -am clean verify

# === BUILD STAGE ===
FROM base AS builder
WORKDIR /app
# Build JAR without running tests again
RUN mvn -pl workout-service -am clean package -DskipTests

# === RUNTIME STAGE ===
FROM eclipse-temurin:21-jdk-jammy AS runner
WORKDIR /app
COPY --from=builder /app/workout-service/target/workout-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
