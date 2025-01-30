## Dashboard Application Backend

### Overview

This is a dashboard application for vehicles that provides real-time updates on various metrics such as battery capacity, temperature, motor speed, RPM, and power input. The application uses WebSocket for real-time communication and schedules tasks to simulate real-world scenarios.  

Prerequisites:
- Java SDK 21
- Spring Boot 3.4.1
- MySQL 8

### Technical Design Document:

https://docs.google.com/document/d/1t4sMQC7izzmQ98fK74Y-Zo6FlTWfywD9GSzChE-UD9s/edit?usp=sharing

### API Documentation
For detailed API documentation, refer to the API Documentation.
https://secret-garden-38447-cfa420a113c0.herokuapp.com/api/v1/api-docs


## Local Dev Setup Instructions:
### **Run the application using Docker
---

Check this repository: https://github.com/curtiskim808/appianway-platform
This setup provides a simple development environment using Docker containers for both frontend and backend applications. If you are not familiar with Java or don't have a Java environment installed, this setup will help you get started quickly without additional configurations.

---
### Or, Run the application on your local machine

1. Create a local MySQL 8 database and refer to the application.properties file or IntelliJ environment variables setting for the required environment variables.
```aiignore
# application.properties

spring.datasource.url=${DB_URL} # update it with your db url jdbc:mysql://localhost:3306/vehicle_dashboard
spring.datasource.username=${DB_USERNAME} # replace it with your db username
spring.datasource.password=${DB_PASSWORD} # replace it with your password

```
   
3. Configure CORS Settings
   To prevent CORS errors, update the CORS configuration in WebSocketConfig.java and WebConfig.java as follows:

```aiignore
// WebSocketConfig.java
public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
            .setAllowedOrigins("http://localhost:5173") // Modify this line 
            .withSockJS()
            .setHeartbeatTime(25000);
}
```

```aiignore
// WebConfig.java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("*") // Modify This line
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            //.allowCredentials(true) // Remove this line
            .allowedHeaders("*")
            .maxAge(3600);
}
```
3. Build and Run Your Application
   Build your application using Maven CMD or your IDE. 

```aiignore
./mvnw clean install
```

Run your application using one of the following commands:
```aiignore
java -jar ./target/dashboard-0.0.1-SNAPSHOT.jar
```
or

```aiignore
./mvnw spring-boot:run
```


Notes:

4. Simulation or Emulation of the Scheduling Job
   The scheduling jobs in the /scheduled folder simulate real-world scenarios by updating data periodically under specific conditions:  
   - When the motor is on
   - When the motor is off and charging is off
   - When charging is on
   The temperature, motor speed, motor RPM, and power input are updated based on these conditions.

5. You can also run the application using Docker. Refer to the Dockerfile for more information.
