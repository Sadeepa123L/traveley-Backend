package com.traveley.traveley_Backend.service.impl;

import com.traveley.traveley_Backend.entity.ChatMessage;
import com.traveley.traveley_Backend.entity.MessageStatus;
import com.traveley.traveley_Backend.repository.ChatMessageRepo;
import com.traveley.traveley_Backend.service.custom.ChatMessageService;
import com.traveley.traveley_Backend.service.custom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageImpl implements ChatMessageService {
    private final ChatRoomService chatRoomService;
    private final ChatMessageRepo chatMessageRepo;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getReceiverId(),
                true
        ).orElseThrow(() -> new RuntimeException("Error creating that room "));

        chatMessage.setChatId(chatId);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setStatus(MessageStatus.RECEIVED);

        return chatMessageRepo.save(chatMessage);
    }

    @Override
    public List<ChatMessage> findChatMessage(Long senderId, Long receiverId) {
        var chatId = chatRoomService.getChatRoomId(senderId, receiverId, false);
        return chatId.map(chatMessageRepo::findByChatId).orElse(new ArrayList<>());
    }

    @Override
    public List<ChatMessage> findConversations(Long userId) {
        List<ChatMessage> allMessages = chatMessageRepo.findBySenderIdOrReceiverId(userId, userId);

        Map<Long, List<ChatMessage>> messagesByContact = allMessages.stream()
                .filter(msg -> msg.getSenderId() != null && msg.getReceiverId() != null)
                .collect(Collectors.groupingBy(msg ->
                        msg.getSenderId().equals(userId) ? msg.getReceiverId() : msg.getSenderId()
                ));
        return messagesByContact.values().stream()
                .map(msgs -> msgs.stream().max(Comparator.comparing(ChatMessage::getTimestamp)).orElse(null))
                .filter(msg -> msg != null)
                .sorted(Comparator.comparing(ChatMessage::getTimestamp).reversed())
                .collect(Collectors.toList());
    }
}
