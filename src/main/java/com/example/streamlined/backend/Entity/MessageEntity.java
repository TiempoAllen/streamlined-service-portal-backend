package com.example.streamlined.backend.Entity;

import com.example.streamlined.backend.Model.MessageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name="tblMessage")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(name="chat_id")
    private Long chatId;

    @Column(name="sender")
    private String sender;

    @Column(name="receiver")
    private String receiver;

    @Column(name="content")
    private String content;

    @Column(name="timestamp")
    private String timestamp;

    @Column(name="attachment")
    private String attachment;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    public MessageEntity() {
    }

    public MessageEntity(Long chatId,String sender, String receiver,String content, String timestamp, String attachment, MessageStatus status) {
        this.chatId = chatId;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.attachment = attachment;
        this.status = status;
    }

    public MessageEntity(Long messageId, Long chatId, String sender, String receiver,String content, String timestamp,
            String attachment, MessageStatus status) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
        this.attachment = attachment;
        this.status = status;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver(){
        return receiver;
    }

    public void setReceiver(String receiver){
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    

    
}