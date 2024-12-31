## Vehicle Dashboard Application

### Overview

This is a dashboard application for vehicles that provides real-time updates on various metrics such as battery capacity, temperature, motor speed, RPM, and power input. The application uses WebSocket for real-time communication and schedules tasks to simulate real-world scenarios.  

Prerequisites:
- Java SDK 21
- Spring Boot 3.4.1
- MySQL 8

### Technical Design Document:

https://docs.google.com/document/d/1t4sMQC7izzmQ98fK74Y-Zo6FlTWfywD9GSzChE-UD9s/edit?usp=sharing


Local Dev Setup Instructions:
1. Create Your Local MySQL Database
   Create a MySQL database and refer to the application.properties file or IntelliJ environment variables setting for the required environment variables.
2. Configure CORS Settings
   To prevent CORS errors, update the CORS configuration in WebSocketConfig.java and WebConfig.java as follows:

```aiignore
// WebSocketConfig.java
public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
            .setAllowedOrigins("*") // Modify this line to have "*"
            .withSockJS()
            .setHeartbeatTime(25000);
}
```

```aiignore
// WebConfig.java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("*") // Modify This line to have "*"
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            //.allowCredentials(true) // Remove this line
            .allowedHeaders("*")
            .maxAge(3600);
}
```
3. Build and Run Your Application
   Build your application using Maven:

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
4. Set Your Environment Variables
   Set the following environment variables in your IntelliJ IDEA environment settings or place them in application.properties:
```aiignore
DB_URL=jdbc:mysql://localhost:3306/<your database name>
DB_USERNAME=<your user name>
DB_PASSWORD=<your password>
FRONTEND_URL=<your front end url>
```

5. Create Database MySQL 8
   Create a MySQL 8 database and update the environment variables with the database name, username, and password.

Notes:
6. Simulation or Emulation of the Scheduling Job
   The scheduling jobs in the /scheduled folder simulate real-world scenarios by updating data periodically under specific conditions:  
   - When the motor is on
   - When the motor is off and charging is off
   - When charging is on
   The temperature, motor speed, motor RPM, and power input are updated based on these conditions.

7. API Documentation
   For detailed API documentation, refer to the API Documentation.
   https://secret-garden-38447-cfa420a113c0.herokuapp.com/api/v1/api-docs
