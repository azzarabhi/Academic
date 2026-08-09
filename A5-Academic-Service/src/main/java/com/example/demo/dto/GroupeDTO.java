package com.example.demo.dto;

public class GroupeDTO {
    private Long id;
    private String nom;
    private Long classeId;

    public GroupeDTO() {}

    public GroupeDTO(Long id, String nom, Long classeId) {
        this.id = id;
        this.nom = nom;
        this.classeId = classeId;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Long getClasseId() {
		return classeId;
	}

	public void setClasseId(Long classeId) {
		this.classeId = classeId;
	}

}
