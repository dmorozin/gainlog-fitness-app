# === BASE BUILD STAGE ===
FROM maven:3.9.9-eclipse-temurin-21 AS base
WORKDIR /app

# === Copy POMs for dependency caching ===
COPY pom.xml .
COPY core/pom.xml core/
COPY proto-common/pom.xml proto-common/
COPY workout-service/pom.xml workout-service/

# === Install internal modules into local Maven repo ===
RUN mvn -pl core,proto-common -am clean install -DskipTests

# === Download dependencies for workout-service only ===
RUN mvn -f workout-service/pom.xml dependency:go-offline -B

# === Copy full source code ===
COPY . .

# === TEST STAGE ===
FROM base AS test
WORKDIR /app
# Run tests for workout-service (includes dependencies from base)
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
