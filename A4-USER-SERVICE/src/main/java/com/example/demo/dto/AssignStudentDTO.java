package com.example.demo.dto;

public class AssignStudentDTO {

    private Long classeId;
    private Long groupeId;

    public AssignStudentDTO() {
    }

    public Long getClasseId() {
        return classeId;
    }

    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }

    public Long getGroupeId() {
        return groupeId;
    }

    public void setGroupeId(Long groupeId) {
        this.groupeId = groupeId;
    }
}