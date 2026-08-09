package com.example.demo.dto;

public class ModuleAssignedRequest {
    private Long teacherId;
    private String teacherEmail;
    private String teacherName;
    private String moduleName;
    private String classeName;

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public String getTeacherEmail() { return teacherEmail; }
    public void setTeacherEmail(String teacherEmail) { this.teacherEmail = teacherEmail; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public String getClasseName() { return classeName; }
    public void setClasseName(String classeName) { this.classeName = classeName; }
}