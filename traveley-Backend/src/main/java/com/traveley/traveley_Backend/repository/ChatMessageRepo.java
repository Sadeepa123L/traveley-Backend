package com.traveley.traveley_Backend.repository;

import com.traveley.traveley_Backend.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepo extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatId(String chatId);
    List<ChatMessage> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}
