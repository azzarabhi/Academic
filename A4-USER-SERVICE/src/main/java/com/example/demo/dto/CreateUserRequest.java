package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {

	    @NotBlank
	    private String nom;

	    @NotBlank
	    private String prenom;

	    @NotBlank
	    private String cin;

	    @Email
	    private String email;
	    @NotBlank
	    private String password;
	
public CreateUserRequest() {
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
public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

}