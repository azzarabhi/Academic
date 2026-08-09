package com.example.demo.dto;

public class AbsenceRecordedRequestDTO {
    private Long studentId;
    private String studentEmail;
    private String studentName;
    private String moduleName;
    private String date;
    private String heure;

    public AbsenceRecordedRequestDTO() {}

    public AbsenceRecordedRequestDTO(Long studentId, String studentEmail, String studentName,
                                      String moduleName, String date, String heure) {
        this.studentId = studentId;
        this.studentEmail = studentEmail;
        this.studentName = studentName;
        this.moduleName = moduleName;
        this.date = date;
        this.heure = heure;
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getHeure() { return heure; }
    public void setHeure(String heure) { this.heure = heure; }
}