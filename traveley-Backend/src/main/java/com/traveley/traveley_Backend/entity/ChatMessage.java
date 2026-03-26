package com.traveley.traveley_Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatId;
    private Long senderId;
    private Long receiverId;
    private String message;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;
}
