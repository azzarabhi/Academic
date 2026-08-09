package com.example.demo.dto.ia;

import java.util.List;

public class StudentAnalysisRequestDTO {
    private Long studentId;
    private String studentName;
    private List<NoteDTO> notes;
    private List<AbsenceDTO> absences;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public List<NoteDTO> getNotes() { return notes; }
    public void setNotes(List<NoteDTO> notes) { this.notes = notes; }
    public List<AbsenceDTO> getAbsences() { return absences; }
    public void setAbsences(List<AbsenceDTO> absences) { this.absences = absences; }

    public static class NoteDTO {
        private Long moduleId;
        private Integer partie;
        private Double noteCC, noteTP, noteExam, moyenne, coefficient;

        public Long getModuleId() { return moduleId; }
        public void setModuleId(Long v) { moduleId = v; }
        public Integer getPartie() { return partie; }
        public void setPartie(Integer v) { partie = v; }
        public Double getNoteCC() { return noteCC; }
        public void setNoteCC(Double v) { noteCC = v; }
        public Double getNoteTP() { return noteTP; }
        public void setNoteTP(Double v) { noteTP = v; }
        public Double getNoteExam() { return noteExam; }
        public void setNoteExam(Double v) { noteExam = v; }
        public Double getMoyenne() { return moyenne; }
        public void setMoyenne(Double v) { moyenne = v; }
        public Double getCoefficient() { return coefficient; }
        public void setCoefficient(Double v) { coefficient = v; }
    }

    public static class AbsenceDTO {
        private Long moduleId;
        private String date;
        private boolean justifiee;

        public Long getModuleId() { return moduleId; }
        public void setModuleId(Long v) { moduleId = v; }
        public String getDate() { return date; }
        public void setDate(String v) { date = v; }
        public boolean isJustifiee() { return justifiee; }
        public void setJustifiee(boolean v) { justifiee = v; }
    }
}