package com.example.streamlined.backend.Entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tblNotifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_id;

    @Column(name = "message")
    private String message;

    @Column(name = "recipient_id")
    private Long recipientId;

    @Column(name = "recipient_role")
    private String recipientRole; // e.g., "Admin", "User"

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "is_read")
    private boolean isRead;

	public NotificationEntity() {

	}

	public NotificationEntity(String message, Long recipientId, String recipientRole) {
        this.message = message;
        this.recipientId = recipientId;
        this.recipientRole = recipientRole;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.isRead = false;
    }

	public Long getNotification_id() {
		return notification_id;
	}

	public void setNotification_id(Long notification_id) {
		this.notification_id = notification_id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}

	public String getRecipientRole() {
		return recipientRole;
	}

	public void setRecipientRole(String recipientRole) {
		this.recipientRole = recipientRole;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}



}