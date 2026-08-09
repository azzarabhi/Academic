	package com.example.demo.entity;
	import jakarta.persistence.*;
	
	
	@Entity
	
	public class Groupe {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String nom;
	    private Long classeId;
		public Groupe(Long id, String nom, Long classeId) {
			super();
			this.id = id;
			this.nom = nom;
			this.classeId = classeId;
		}
		public Groupe() {}
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