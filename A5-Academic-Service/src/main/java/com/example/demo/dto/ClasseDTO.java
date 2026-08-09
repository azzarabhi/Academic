package com.example.demo.dto;

public class ClasseDTO {
	   private Long id;
	    private String nom;
	    private String niveau;
	    private String specialite;

	    public ClasseDTO() {}

	    public ClasseDTO(Long id, String nom, String niveau, String specialite) {
	        this.id = id;
	        this.nom = nom;
	        this.niveau = niveau;
	        this.specialite = specialite;
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
