package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.Repository.ModuleRepository;
import com.example.demo.Service.AcademicService;
import com.example.demo.dto.StudentProfileDTO;
import com.example.demo.entity.*;
import com.example.demo.entity.Module;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/academic")
public class AcademicController {
	private final ModuleRepository moduleRepository;
    private final AcademicService service;

   

    public AcademicController(ModuleRepository moduleRepository, AcademicService service) {
		super();
		this.moduleRepository = moduleRepository;
		this.service = service;
	}

	// ================= MODULE =================
    @GetMapping("/modules")
    public List<Module> getModules() {
        return service.getAllModules();
    }

    @PostMapping("/modules")
    public Module addModule(@RequestBody Module m) {
        return service.addModule(m);
    }
    @GetMapping("/modules/classe/{classeId}")
    public List<Module> getModulesByClasse(@PathVariable Long classeId) {
        return moduleRepository.findByClasseId(classeId);
    }

    @PutMapping("/modules/{id}")
    public Module updateModule(@PathVariable Long id, @RequestBody Module module) {
        return service.updateModule(id, module);
    }

    @DeleteMapping("/modules/{id}")
    public void deleteModule(@PathVariable Long id) {
        service.deleteModule(id);
    }

    // ================= CLASSE =================
    @GetMapping("/classes")
    public List<Classe> getClasses() {
        return service.getAllClasses();
    }

    @PostMapping("/classes")
    public Classe addClasse(@RequestBody Classe c) {
        return service.addClasse(c);
    }

    @PutMapping("/classes/{id}")
    public Classe updateClasse(@PathVariable Long id, @RequestBody Classe c) {
        return service.updateClasse(id, c);
    }

    @DeleteMapping("/classes/{id}")
    public void deleteClasse(@PathVariable Long id) {
        service.deleteClasse(id);
    }

    // ================= GROUPE =================
    @GetMapping("/groupes")
    public List<Groupe> getGroupes() {
        return service.getAllGroupes();
    }

    @PostMapping("/groupes")
    public Groupe addGroupe(@RequestBody Groupe g) {
        return service.addGroupe(g);
    }

    @PutMapping("/groupes/{id}")
    public Groupe updateGroupe(@PathVariable Long id, @RequestBody Groupe g) {
        return service.updateGroupe(id, g);
    }

    @DeleteMapping("/groupes/{id}")
    public void deleteGroupe(@PathVariable Long id) {
        service.deleteGroupe(id);
    }

    // ================= NOTE =================
    @GetMapping("/notes")
    public List<Note> getAllNotes() {
        return service.getAllNotes();
    }

    @GetMapping("/notes/{studentId}")
    public List<Note> getNotes(@PathVariable Long studentId) {
        return service.getNotesByStudent(studentId);
    }

    @PostMapping("/notes")
    public Note addNote(@RequestBody Note n) {
        return service.addNote(n);
    }

    @PutMapping("/notes/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody Note n) {
        return service.updateNote(id, n);
    }

    @DeleteMapping("/notes/{id}")
    public void deleteNote(@PathVariable Long id) {
        service.deleteNote(id);
    }

    // ================= ABSENCE =================
    @GetMapping("/absences")
    public List<Absence> getAllAbsences() {
        return service.getAllAbsences();
    }

    @GetMapping("/absences/{studentId}")
    public List<Absence> getAbsences(@PathVariable Long studentId) {
        return service.getAbsencesByStudent(studentId);
    }

    @PostMapping("/absences")
    public Absence addAbsence(@RequestBody Absence a) {
        return service.addAbsence(a);
    }
    @PostMapping("/absences/bulk")
    public List<Absence> addAbsences(@RequestBody List<Absence> absences) {
        return service.addAbsences(absences);
    }
    @PutMapping("/absences/{id}")
    public Absence updateAbsence(@PathVariable Long id, @RequestBody Absence a) {
        return service.updateAbsence(id, a);
    }

    @DeleteMapping("/absences/{id}")
    public void deleteAbsence(@PathVariable Long id) {
        service.deleteAbsence(id);
    }

    // ================= PROFILE =================
    @GetMapping("/profile/{studentId}")
    public StudentProfileDTO getProfile(@PathVariable Long studentId) {
        return service.getStudentProfile(studentId);
    }
 // ===== TEACHER ENDPOINTS =====
    @GetMapping("/modules/teacher/{teacherId}")
    public List<Module> getModulesByTeacher(@PathVariable Long teacherId) {
        return service.getModulesByTeacher(teacherId);
    }

    @GetMapping("/notes/teacher/{teacherId}")
    public List<Note> getNotesByTeacher(@PathVariable Long teacherId) {
        List<Long> moduleIds = service.getModulesByTeacher(teacherId)
                .stream().map(m -> m.getId()).collect(java.util.stream.Collectors.toList());
        return service.getNotesByModules(moduleIds);
    }

    @GetMapping("/absences/teacher/{teacherId}")
    public List<Absence> getAbsencesByTeacher(@PathVariable Long teacherId) {
        List<Long> moduleIds = service.getModulesByTeacher(teacherId)
                .stream().map(m -> m.getId()).collect(java.util.stream.Collectors.toList());
        return service.getAbsencesByModules(moduleIds);
    }
    
    
    @GetMapping("/modules/classe/{classeId}/teachers")
    public List<Long> getTeacherIdsByClasse(@PathVariable Long classeId) {
        return moduleRepository.findByClasseId(classeId).stream()
                .map(Module::getTeacherId)
                .filter(t -> t != null)
                .distinct()
                .collect(Collectors.toList());
    }
    
  
 /* ================= TEACHER-GROUPE =================
    @GetMapping("/teacher-groupes")
    public List<TeacherGroupe> getAllTeacherGroupes() {
        return service.getAllTeacherGroupes();
    }

    @GetMapping("/teacher-groupes/teacher/{teacherId}")
    public List<TeacherGroupe> getTeacherGroupes(@PathVariable Long teacherId) {
        return service.getTeacherGroupes(teacherId);
    }

    @PostMapping("/teacher-groupes")
    public TeacherGroupe addTeacherGroupe(@RequestBody TeacherGroupe tg) {
        return service.addTeacherGroupe(tg);
    }

    @DeleteMapping("/teacher-groupes/{id}")
    public void deleteTeacherGroupe(@Path:Variable Long id) {
        service.deleteTeacherGroupe(id);
    }*/
    
   
}