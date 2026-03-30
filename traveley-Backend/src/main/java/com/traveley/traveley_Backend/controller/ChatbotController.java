package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.service.impl.ChatbotToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {
     private final ChatClient chatClient;
     private final ChatbotToolService chatbotToolService;

     @GetMapping("/ask")
    public String chatWithAi(@RequestParam String message){
         try {
             return this.chatClient.prompt()
                     .user(message)
                     .tools(chatbotToolService)
                     .call()
                     .content();
         } catch (Exception e) {
             e.printStackTrace();
             throw new RuntimeException(e);
         }
     }
}
