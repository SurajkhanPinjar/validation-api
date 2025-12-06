# ============================
# 1. Build Stage
# ============================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy only dependency files first (Maven cache boost)
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Copy source and build JAR
COPY src ./src
RUN mvn -q package -DskipTests

# ============================
# 2. Runtime Stage
# ============================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built JAR from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose port used in application.yaml
EXPOSE 8081

# Use environment variables if needed
ENV JAVA_OPTS=""

# Run app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]