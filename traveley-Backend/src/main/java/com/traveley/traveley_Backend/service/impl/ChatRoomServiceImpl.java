package com.traveley.traveley_Backend.service.impl;

import com.traveley.traveley_Backend.entity.ChatRoom;
import com.traveley.traveley_Backend.repository.ChatRoomRepo;
import com.traveley.traveley_Backend.service.custom.ChatRoomService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepo chatRoomRepo;
    @Override
    public Optional<String> getChatRoomId(Long senderId, Long receiverId, boolean createNewRoomIfNotExists) {
        Optional<ChatRoom> existingRooms = chatRoomRepo.findFirstBySenderIdAndReceiverId(senderId, receiverId);
        if (existingRooms.isPresent()) {
            return Optional.of(existingRooms.get().getChatId());
        }

        if (createNewRoomIfNotExists) {
            String chatId = String.format("%s_%s", senderId, receiverId);

            ChatRoom senderRecipient = ChatRoom.builder()
                    .chatId(chatId)
                    .senderId(senderId)
                    .receiverId(receiverId)
                    .build();
            ChatRoom recipientSender = ChatRoom.builder()
                    .chatId(chatId)
                    .senderId(receiverId)
                    .receiverId(senderId)
                    .build();
            chatRoomRepo.save(senderRecipient);
            chatRoomRepo.save(recipientSender);

            return Optional.of(chatId);
    }
        return Optional.empty();

//    private String createChatId (Long senderId, Long receiverId) {
//        var chatId = String.format("%d_%d", senderId, receiverId);
//
//        ChatRoom senderRecipient = ChatRoom.builder().chatId(chatId).senderId(senderId).receiverId(receiverId).build();
//        ChatRoom recipientSender = ChatRoom.builder().chatId(chatId).senderId(receiverId).receiverId(senderId).build();
//
//        chatRoomRepo.save(senderRecipient);
//        chatRoomRepo.save(recipientSender);
//
//        return chatId;
  }
}
