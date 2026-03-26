package com.traveley.traveley_Backend.service.custom;


import java.util.Optional;

public interface ChatRoomService {
    Optional<String> getChatRoomId(Long senderId, Long receiverId, boolean createNewRoomIfNotExists);
}
