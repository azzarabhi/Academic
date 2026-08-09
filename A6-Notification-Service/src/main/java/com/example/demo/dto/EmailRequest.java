package com.example.demo.dto;

public class EmailRequest {
    private Long userId;   // optionnel, pour historique
    private String to;
    private String subject;
    private String body;

    public EmailRequest() {}
    public EmailRequest(Long userId, String to, String subject, String body) {
        this.userId = userId; this.to = to; this.subject = subject; this.body = body;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}