package com.example.streamlined.backend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull; // Import for @NonNull annotation
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


// import com.example.streamlined.backend.Websocket.ChatHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue").setHeartbeatValue(new long[] {10000, 10000}).setTaskScheduler(heartBeatScheduler());; // Message broker for subscriptions
        config.setApplicationDestinationPrefixes("/app"); // Prefix for client-to-server messages
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS(); // WebSocket endpoint
        registry.addEndpoint("/notification").setAllowedOriginPatterns("*").withSockJS();
    }

    @Bean
    public ThreadPoolTaskScheduler heartBeatScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1); // Only one thread is needed for heartbeats
        taskScheduler.setThreadNamePrefix("wss-heartbeat-thread-");
        return taskScheduler;
    }
}