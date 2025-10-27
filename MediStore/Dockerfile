# ---------- Step 1: Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy Maven config and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the jar (skip tests for speed)
RUN mvn clean package -DskipTests



# ---------- Step 2: Runtime Stage ----------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy only the built jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run
ENTRYPOINT ["java", "-jar", "app.jar"]
