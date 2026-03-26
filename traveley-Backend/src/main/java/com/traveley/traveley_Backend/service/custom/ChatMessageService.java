package com.traveley.traveley_Backend.service.custom;

import com.traveley.traveley_Backend.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    ChatMessage save(ChatMessage chatMessage);
    List<ChatMessage> findChatMessage(Long senderId, Long receiverId);
    List<ChatMessage> findConversations(Long userId);
}
