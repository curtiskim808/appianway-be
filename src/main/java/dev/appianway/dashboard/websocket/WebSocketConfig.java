package dev.appianway.dashboard.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${frontend.allowed-origins}")
    private String frontendOrigin;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Define the endpoint clients will use to connect
        registry.addEndpoint("/ws")
                .setAllowedOrigins(frontendOrigin)
                .withSockJS() // Fallback options for browsers that don’t support WebSocket
                .setHeartbeatTime(25000);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Define the message broker prefixes
        registry.enableSimpleBroker("/topic") // For broadcasting messages
                .setHeartbeatValue(new long[]{10000, 10000}) // Set heartbeat
                .setTaskScheduler(heartBeatScheduler()); // Custom scheduler
        registry.setApplicationDestinationPrefixes("/app"); // For client-to-server messages
    }
    @Bean
    public ThreadPoolTaskScheduler heartBeatScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("wss-heartbeat-");
        return scheduler;
    }
}
