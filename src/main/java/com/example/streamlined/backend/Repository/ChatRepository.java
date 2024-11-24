package com.example.streamlined.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.streamlined.backend.Entity.ChatEntity;
import com.example.streamlined.backend.Entity.UserEntity;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long>{
    // List<ChatEntity> findByChat(Long chatId);
    ChatEntity findByName(String name);
    List<ChatEntity> findByParticipants(UserEntity username);
    
}