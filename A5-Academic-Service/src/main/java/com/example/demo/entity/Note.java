package com.example.demo.entity;
import jakarta.persistence.*;
@Entity

public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    private Long moduleId;

    private Double noteCC;

    private Double noteTP;

    private Double noteExam;

    private Double moyenne;
    
	public Note(Long id, Long studentId, Long moduleId, Double noteCC, Double noteTP, Double noteExam, Double moyenne) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.moduleId = moduleId;
		this.noteCC = noteCC;
		this.noteTP = noteTP;
		this.noteExam = noteExam;
		this.moyenne = moyenne;
	}

	public Note() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Double getNoteCC() {
		return noteCC;
	}

	public void setNoteCC(Double noteCC) {
		this.noteCC = noteCC;
	}

	public Double getNoteTP() {
		return noteTP;
	}

	public void setNoteTP(Double noteTP) {
		this.noteTP = noteTP;
	}

	public Double getNoteExam() {
		return noteExam;
	}

	public void setNoteExam(Double noteExam) {
		this.noteExam = noteExam;
	}

	public Double getMoyenne() {
		return moyenne;
	}

	public void setMoyenne(Double moyenne) {
		this.moyenne = moyenne;
	}
}
    
