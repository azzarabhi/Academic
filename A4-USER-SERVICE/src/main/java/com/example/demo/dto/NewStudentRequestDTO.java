package com.example.demo.dto;

public class NewStudentRequestDTO {
    private Long teacherId;
    private String teacherEmail;
    private String teacherName;
    private String studentName;
    private String classeName;
    private String groupeName;

    public NewStudentRequestDTO() {}
    public NewStudentRequestDTO(Long teacherId, String teacherEmail, String teacherName,
                                 String studentName, String classeName, String groupeName) {
        this.teacherId = teacherId; this.teacherEmail = teacherEmail; this.teacherName = teacherName;
        this.studentName = studentName; this.classeName = classeName; this.groupeName = groupeName;
    }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public String getTeacherEmail() { return teacherEmail; }
    public void setTeacherEmail(String teacherEmail) { this.teacherEmail = teacherEmail; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getClasseName() { return classeName; }
    public void setClasseName(String classeName) { this.classeName = classeName; }
    public String getGroupeName() { return groupeName; }
    public void setGroupeName(String groupeName) { this.groupeName = groupeName; }
}