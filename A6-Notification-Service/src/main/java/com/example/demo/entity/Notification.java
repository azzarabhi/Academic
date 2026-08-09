package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String type;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String channel;
    private boolean sent;

    @Column(name = "is_read")   // ✅ "read" est un mot réservé MySQL → renommé en colonne
    private boolean read;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    private LocalDateTime createdAt;

    public Notification() {}

    public Notification(Long userId, String type, String title, String message, String channel) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.channel = channel;
        this.sent = false;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
    public boolean isSent() { return sent; }
    public void setSent(boolean sent) { this.sent = sent; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}