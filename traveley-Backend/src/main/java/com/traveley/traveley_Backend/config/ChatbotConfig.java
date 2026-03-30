package com.traveley.traveley_Backend.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatbotConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("You are a smart travel agent for the company 'Travely'. " +
                "Use the provided functions to check available packages and book tours for users. " +
                "Do not make up any prices or tour names.")
                .build();
    }
}
