package com.example.streamlined.backend.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.streamlined.backend.Entity.MessageEntity;
import com.example.streamlined.backend.Model.MessageStatus;
import com.example.streamlined.backend.Repository.MessageRepository;

@Service
public class MessageService {
  
    @Autowired
    private MessageRepository mrepo;

    public MessageEntity createMessage(MessageEntity message){
        return mrepo.save(message);
    }

    public List<MessageEntity> getAllMessages(){
        return mrepo.findAll();
    }

    public List<MessageEntity> getMessagesByChatId(Long chatId){
        return mrepo.findByChatId(chatId);
    }

    public Optional<MessageEntity> getMessageById(Long messageId){
        return mrepo.findById(messageId);
    }

    public MessageEntity updateMessage(Long messageId, MessageEntity messageDetails){
        Optional<MessageEntity> optionalMessage = mrepo.findById(messageId);
        if(optionalMessage.isPresent()){
            MessageEntity message = optionalMessage.get();
            message.setContent(messageDetails.getContent());
            message.setTimestamp(messageDetails.getTimestamp());
            message.setStatus(messageDetails.getStatus());
            return mrepo.save(message);
        }else{
            throw new RuntimeException("Message not found");
        }
    }

    public void deleteMessage(Long messageId){
        mrepo.deleteById(messageId);
    }

    

    public List<MessageEntity> getUnreadMessagesForUser(Long userId) {
        return mrepo.findByReceiverAndStatus(userId.toString(), MessageStatus.SENT);
    }

    public List<MessageEntity> getUnreadAndRecentMessagesForUser(Long userId) {
        String receiverId = userId.toString();
    
        // Fetch unread messages
        List<MessageEntity> unreadMessages = mrepo.findByReceiverAndStatus(receiverId, MessageStatus.SENT);
    
        // Calculate the date from 7 days ago and format it as a String
        Timestamp sevenDaysAgo = Timestamp.from(Instant.now().minus(7, ChronoUnit.DAYS));
        String formattedTimestamp = new SimpleDateFormat("yyyy/MM/dd - HH:mm").format(sevenDaysAgo);
    
        // Fetch recent messages from the last 7 days using formatted timestamp
        List<MessageEntity> recentMessages = mrepo.findRecentMessagesForUser(receiverId, formattedTimestamp);
    
        // Remove any recent messages that are also unread to avoid duplicates
        recentMessages.removeAll(unreadMessages);
    
        // Combine the lists: unread + recent messages
        unreadMessages.addAll(recentMessages);
    
        // Define date formatter for parsing timestamp strings
       // Define date formatter for parsing timestamp strings
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd - H:mm");  // Using 24-hour format

        // Sort messages by timestamp, with recent messages appearing first (newest to oldest)
        Collections.sort(unreadMessages, new Comparator<MessageEntity>() {
            @Override
            public int compare(MessageEntity m1, MessageEntity m2) {
                try {
                    LocalDateTime dateTime1 = LocalDateTime.parse(m1.getTimestamp(), formatter);
                    LocalDateTime dateTime2 = LocalDateTime.parse(m2.getTimestamp(), formatter);
                    return dateTime2.compareTo(dateTime1);  // Descending order (newest first)
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                    return 0;  // If parsing fails, treat timestamps as equal
                }
            }
        });

    
        return unreadMessages;  // Return combined and sorted list
    }

    
    public void markMessagesAsRead(List<Long> messageIds) {
        List<MessageEntity> messages = mrepo.findAllById(messageIds);
        for (MessageEntity message : messages) {
            message.setStatus(MessageStatus.READ);
        }
        mrepo.saveAll(messages);
    }


    // public List<MessageEntity> getRecentMessagesForUser(Long userId, String sender) {
    //     return mrepo.findAllByReceiverOrSender(userId, sender);
    // }
    
    
    
}