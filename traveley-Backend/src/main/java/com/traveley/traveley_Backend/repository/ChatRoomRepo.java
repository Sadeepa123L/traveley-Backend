package com.traveley.traveley_Backend.repository;

import com.traveley.traveley_Backend.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepo extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findFirstBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
