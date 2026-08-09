package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.client.AiClient;
import com.example.demo.client.UserClient;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.ia.StudentAnalysisRequestDTO;
import com.example.demo.dto.ia.StudentAnalysisResponseDTO;
import com.example.demo.entity.Note;
import com.example.demo.entity.Absence;
import com.example.demo.Repository.NoteRepository;
import com.example.demo.Repository.AbsenceRepository;
import com.example.demo.Repository.ModuleRepository;

@RestController
@RequestMapping("/academic/ai")
public class AiAnalysisController {

    @Autowired private AiClient aiClient;
    @Autowired private NoteRepository noteRepository;
    @Autowired private AbsenceRepository absenceRepository;
    @Autowired private ModuleRepository moduleRepository;
    @Autowired private UserClient userClient;

    // ===== HELPER: build request pour un étudiant =====
    private StudentAnalysisRequestDTO buildRequest(UserDTO student) {
        Long studentId = student.getId();
        List<Note> notes = noteRepository.findByStudentId(studentId);
        List<Absence> absences = absenceRepository.findByStudentId(studentId);

        StudentAnalysisRequestDTO req = new StudentAnalysisRequestDTO();
        req.setStudentId(studentId);
        req.setStudentName(student.getNom() + " " + student.getPrenom());

        req.setNotes(notes.stream().map(n -> {
            StudentAnalysisRequestDTO.NoteDTO dto = new StudentAnalysisRequestDTO.NoteDTO();
            dto.setModuleId(n.getModuleId());
            dto.setPartie(null);           // ← partie mawjudch fil Note entity — null
            dto.setNoteCC(n.getNoteCC());
            dto.setNoteTP(n.getNoteTP());
            dto.setNoteExam(n.getNoteExam());
            dto.setMoyenne(n.getMoyenne());
            moduleRepository.findById(n.getModuleId())
                .ifPresent(m -> dto.setCoefficient(m.getCoefficient()));
            return dto;
        }).collect(Collectors.toList()));

        req.setAbsences(absences.stream().map(a -> {
            StudentAnalysisRequestDTO.AbsenceDTO dto = new StudentAnalysisRequestDTO.AbsenceDTO();
            dto.setModuleId(a.getModuleId());
            dto.setDate(a.getDate() != null ? a.getDate().toString() : null);
            dto.setJustifiee(a.isJustifiee());
            return dto;
        }).collect(Collectors.toList()));

        return req;
    }

    // ===== ANALYSE UN ÉTUDIANT =====
    @GetMapping("/student/{studentId}")
    public StudentAnalysisResponseDTO analyzeStudent(@PathVariable Long studentId) {
        UserDTO student = userClient.getUserById(studentId);
        return aiClient.analyzeStudent(buildRequest(student));
    }

    // ===== ANALYSE TOUS LES ÉTUDIANTS D'UNE CLASSE =====
    // ← FIX: mش yesta3mel getStudentsByClasse (mawjudch)
    // yesta3mel getAllUsers w yfilter by classeId
    @GetMapping("/classe/{classeId}")
    public List<StudentAnalysisResponseDTO> analyzeClasse(@PathVariable Long classeId) {
        List<UserDTO> allUsers = userClient.getAllUsers();
        List<UserDTO> students = allUsers.stream()
            .filter(u -> "STUDENT".equals(u.getRole())
                      && classeId.equals(u.getClasseId()))
            .collect(Collectors.toList());

        List<StudentAnalysisResponseDTO> results = new ArrayList<>();
        for (UserDTO student : students) {
            try {
                results.add(aiClient.analyzeStudent(buildRequest(student)));
            } catch (Exception e) {
                System.err.println("⚠️ Analyse échouée pour " + student.getNom()
                    + ": " + e.getMessage());
            }
        }
        return results;
    }

    // ===== ANALYSE TOUS LES ÉTUDIANTS =====
    @GetMapping("/all")
    public List<StudentAnalysisResponseDTO> analyzeAll() {
        List<UserDTO> allUsers = userClient.getAllUsers();
        List<UserDTO> students = allUsers.stream()
            .filter(u -> "STUDENT".equals(u.getRole()))
            .collect(Collectors.toList());

        List<StudentAnalysisResponseDTO> results = new ArrayList<>();
        for (UserDTO student : students) {
            try {
                results.add(aiClient.analyzeStudent(buildRequest(student)));
            } catch (Exception e) {
                System.err.println("⚠️ Analyse échouée pour " + student.getNom()
                    + ": " + e.getMessage());
            }
        }
        return results;
    }
}