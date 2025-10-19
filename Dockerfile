# Multi-stage build for search-service
FROM gradle:8.5-jdk17 AS build

# Set working directory
WORKDIR /app

# Copy gradle configuration files first for better caching
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./
RUN chmod +x ./gradlew

# Download dependencies first (better caching)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build the application with optimized settings for Docker
RUN ./gradlew bootJar --no-daemon --max-workers=1 \
    -Dorg.gradle.jvmargs="-Xmx512m -XX:MaxMetaspaceSize=256m"

# Runtime stage
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Install curl for health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create non-root user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copy the built jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Change ownership to non-root user
RUN chown appuser:appuser app.jar

# Switch to non-root user
USER appuser

# Expose the port
EXPOSE 8084

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8084/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
