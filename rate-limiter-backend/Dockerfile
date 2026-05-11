# ----------------------------------------
# Base Image
# Use lightweight Java runtime
# ----------------------------------------
FROM eclipse-temurin:26-jdk

# ----------------------------------------
# Application metadata
# ----------------------------------------
LABEL maintainer="Utkarsh Goswami"
LABEL application="rate-limiter-backend"

# ----------------------------------------
# Set working directory inside container
# ----------------------------------------
WORKDIR /app

# ----------------------------------------
# Copy built JAR from target folder
#
# IMPORTANT:
# Run mvn clean package first
# before docker build
# ----------------------------------------
COPY target/rate-limiter-backend-0.0.1-SNAPSHOT.jar app.jar

# ----------------------------------------
# Expose application port
# ----------------------------------------
EXPOSE 8080

# ----------------------------------------
# Run Spring Boot application
# ----------------------------------------
ENTRYPOINT ["java", "-jar", "app.jar"]