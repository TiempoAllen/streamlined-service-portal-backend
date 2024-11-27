package com.example.streamlined.backend.Entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tblChat")
public class ChatEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_id")
    private Long chatId;

    @Column(name="name")
    private String name;

    @ManyToMany
    @JoinTable(
    name = "chat_participants", 
    joinColumns = @JoinColumn(name = "chat_id"), 
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> participants;

    public ChatEntity() {
    }

    public ChatEntity(String name) {
        this.name = name;
    }

    public ChatEntity(Long chatId, String name) {
        this.chatId = chatId;
        this.name = name;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserEntity> participants) {
        this.participants = participants;
    }
    

}
