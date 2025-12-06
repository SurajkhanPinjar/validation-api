# ============================
# 1. Build Stage
# ============================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom and download dependencies
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Copy project source
COPY src ./src

# Build JAR
RUN mvn -q package -DskipTests

# ============================
# 2. Runtime Stage
# ============================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy built artifact
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]