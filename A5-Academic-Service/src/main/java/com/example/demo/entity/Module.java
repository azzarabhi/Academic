package com.example.demo.entity;

import jakarta.persistence.*;



@Entity

public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String nom;

    private Double coefficient;
    private Long teacherId; 
    private Long classeId;  // ← ZIDHA
    private Integer partie; 
   
   

	public Module(Long id, String code, String nom, Double coefficient, Long teacherId, Long classeId, Integer partie) {
		super();
		this.id = id;
		this.code = code;
		this.nom = nom;
		this.coefficient = coefficient;
		this.teacherId = teacherId;
		this.classeId = classeId;
		this.partie = partie;
	}

	public Module() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Double getCoefficient() { return coefficient; }
    public void setCoefficient(Double coefficient) { this.coefficient = coefficient; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public Long getClasseId() { return classeId; }
    public void setClasseId(Long classeId) { this.classeId = classeId; }
    public Integer getPartie() { return partie; }
    public void setPartie(Integer partie) { this.partie = partie; }

}