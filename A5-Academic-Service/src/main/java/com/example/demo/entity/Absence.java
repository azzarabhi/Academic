package com.example.demo.entity;
import java.time.LocalDate;


import jakarta.persistence.*;

@Entity

public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    private Long moduleId;

    private LocalDate date;

    private boolean justifiee;
    private String heure;



	public Absence(Long id, Long studentId, Long moduleId, LocalDate date, boolean justifiee, String heure) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.moduleId = moduleId;
		this.date = date;
		this.justifiee = justifiee;
		this.heure = heure;
	}

	public Absence() {}

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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public boolean isJustifiee() {
		return justifiee;
	}

	public void setJustifiee(boolean justifiee) {
		this.justifiee = justifiee;
	}
	public String getHeure() { return heure; }
	public void setHeure(String heure) { this.heure = heure; }
    
}