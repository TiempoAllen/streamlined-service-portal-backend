package com.example.streamlined.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.streamlined.backend.Entity.MessageEntity;
import com.example.streamlined.backend.Model.MessageStatus;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long>{

    List<MessageEntity> findByChatId(Long chatId);
    List<MessageEntity> findAllByMessageId(Long messageId);
    List<MessageEntity> findByReceiverAndStatus(String receiver, MessageStatus status);
    // List<MessageEntity> findAllByReceiverOrSender(Long userId, String sender);
    @Query("SELECT m FROM MessageEntity m WHERE m.receiver = :receiverId AND m.timestamp >= :timestamp")
    List<MessageEntity> findRecentMessagesForUser(@Param("receiverId") String receiver, @Param("timestamp") String timestamp);


}