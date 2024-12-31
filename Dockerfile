# Use OpenJDK image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy compiled jar
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]