package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.entity.ChatMessage;
import com.traveley.traveley_Backend.service.custom.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void sendChatMessage(@Payload ChatMessage chatMessage) {
        ChatMessage saveMessage = chatMessageService.save(chatMessage);

        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getReceiverId()),
                "/queue/messages",
                saveMessage
        );
    }

    @GetMapping("/messages/{senderId}/{receiverId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable Long senderId, @PathVariable Long receiverId){
        try {
            List<ChatMessage> chatMessageList = chatMessageService.findChatMessage(senderId, receiverId);
            return ResponseEntity.ok(chatMessageList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<ChatMessage>> getConversations(@PathVariable Long userId){
        try {
            List<ChatMessage> recentChats = chatMessageService.findConversations(userId);
            return ResponseEntity.ok(recentChats);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
