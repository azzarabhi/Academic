package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.*;
import com.example.demo.entity.Module;


public class StudentProfileDTO {

    private UserDTO user;
    private ClasseDTO classe;
    private GroupeDTO groupe;
    private List<Note> notes;
    private List<Absence> absences;
    private List<Module> modules;
    
    
	public StudentProfileDTO() {}


	public UserDTO getUser() {
		return user;
	}


	public void setUser(UserDTO user) {
		this.user = user;
	}


	



	public List<Note> getNotes() {
		return notes;
	}


	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}


	public List<Absence> getAbsences() {
		return absences;
	}


	public void setAbsences(List<Absence> absences) {
		this.absences = absences;
	}


	public List<Module> getModules() {
		return modules;
	}


	public void setModules(List<Module> modules) {
		this.modules = modules;
	}


	public ClasseDTO getClasse() {
		return classe;
	}


	public void setClasse(ClasseDTO classe) {
		this.classe = classe;
	}


	public GroupeDTO getGroupe() {
		return groupe;
	}


	public void setGroupe(GroupeDTO groupe) {
		this.groupe = groupe;
	}


	





}