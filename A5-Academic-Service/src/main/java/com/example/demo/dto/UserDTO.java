package com.example.demo.dto;

public class UserDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String cin;
    private String email;
    private String role;
    private Long classeId;
    private Long groupeId;
    private String photoUrl;   // ← ZEYDA


	public UserDTO(Long id, String nom, String prenom, String cin, String email, String role, Long classeId,
			Long groupeId, String photoUrl) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.cin = cin;
		this.email = email;
		this.role = role;
		this.classeId = classeId;
		this.groupeId = groupeId;
		this.photoUrl = photoUrl;
	}
	public UserDTO() {}
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
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getCin() {
		return cin;
	}
	public void setCin(String cin) {
		this.cin = cin;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
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

	public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }	  
}

	   