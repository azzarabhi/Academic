package com.example.demo.dto;

public class AiAlertRequest {
    private Long studentId;
    private String studentEmail;
    private String studentName;
    private String riskLevel;
    private String message;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}