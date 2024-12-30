package dev.appianway.dashboard.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Define the endpoint clients will use to connect
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5174")
                .withSockJS(); // Fallback options for browsers that don’t support WebSocket
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Define the message broker prefixes
        registry.enableSimpleBroker("/topic"); // For broadcasting messages
        registry.setApplicationDestinationPrefixes("/app"); // For client-to-server messages
    }
}
