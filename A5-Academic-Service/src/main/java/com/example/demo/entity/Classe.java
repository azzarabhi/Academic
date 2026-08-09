package com.example.demo.entity;
import jakarta.persistence.*;

@Entity

public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String niveau;

    private String specialite;

	public Classe(Long id, String nom, String niveau, String specialite) {
		super();
		this.id = id;
		this.nom = nom;
		this.niveau = niveau;
		this.specialite = specialite;
	}

	public Classe() {}

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

	public String getNiveau() {
		return niveau;
	}

	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}

	public String getSpecialite() {
		return specialite;
	}

	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}
	
    
}