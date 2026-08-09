package com.example.demo.dto;

public class ModuleAssignedRequestDTO {
    private Long teacherId;
    private String teacherEmail;
    private String teacherName;
    private String moduleName;
    private String classeName;

    public ModuleAssignedRequestDTO() {}
    public ModuleAssignedRequestDTO(Long teacherId, String teacherEmail, String teacherName,
                                     String moduleName, String classeName) {
        this.teacherId = teacherId; this.teacherEmail = teacherEmail; this.teacherName = teacherName;
        this.moduleName = moduleName; this.classeName = classeName;
    }

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