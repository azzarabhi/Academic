package com.example.demo.Service;
import java.util.List;
import com.example.demo.dto.StudentProfileDTO;

import com.example.demo.entity.Absence;
import com.example.demo.entity.Classe;
import com.example.demo.entity.Groupe;
import com.example.demo.entity.Module;
import com.example.demo.entity.Note;

public interface AcademicService {

    // ===== MODULE =====
    List<Module> getAllModules();
    Module addModule(Module module);
    Module updateModule(Long id, Module module);
    void deleteModule(Long id);

    // ===== CLASSE =====
    List<Classe> getAllClasses();
    Classe addClasse(Classe classe);
    Classe updateClasse(Long id, Classe classe);
    void deleteClasse(Long id);

    // ===== GROUPE =====
    List<Groupe> getAllGroupes();
    Groupe addGroupe(Groupe groupe);
    Groupe updateGroupe(Long id, Groupe groupe);
    void deleteGroupe(Long id);

    // ===== NOTE =====
    List<Note> getAllNotes();
    List<Note> getNotesByStudent(Long studentId);
    Note addNote(Note note);
    Note updateNote(Long id, Note note);
    void deleteNote(Long id);

    // ===== ABSENCE =====
    List<Absence> getAllAbsences();
    List<Absence> getAbsencesByStudent(Long studentId);
    Absence addAbsence(Absence absence);
    List<Absence> addAbsences(List<Absence> absences); 
    Absence updateAbsence(Long id, Absence absence);
    void deleteAbsence(Long id);
 // ===== TEACHER-SPECIFIC =====
    List<Module> getModulesByTeacher(Long teacherId);
    List<Note> getNotesByModules(List<Long> moduleIds);
    List<Absence> getAbsencesByModules(List<Long> moduleIds);
    // ===== PROFILE =====
    StudentProfileDTO getStudentProfile(Long studentId);
 /* ===== TEACHER-GROUPE =====
    List<TeacherGroupe> getAllTeacherGroupes();
    List<TeacherGroupe> getTeacherGroupes(Long teacherId);
    TeacherGroupe addTeacherGroupe(TeacherGroupe tg);
    void deleteTeacherGroupe(Long id);*/
}