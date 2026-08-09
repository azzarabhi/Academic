package com.example.demo.dto;

public class NotePublishedRequestDTO {
    private Long studentId;
    private String studentEmail;
    private String studentName;
    private String moduleName;
    private Double moyenne;

    public NotePublishedRequestDTO() {}

    public NotePublishedRequestDTO(Long studentId, String studentEmail, String studentName,
                                    String moduleName, Double moyenne) {
        this.studentId = studentId;
        this.studentEmail = studentEmail;
        this.studentName = studentName;
        this.moduleName = moduleName;
        this.moyenne = moyenne;
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public Double getMoyenne() { return moyenne; }
    public void setMoyenne(Double moyenne) { this.moyenne = moyenne; }
}