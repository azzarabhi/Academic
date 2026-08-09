package com.example.demo.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.Repository.AbsenceRepository;
import com.example.demo.Repository.ClasseRepository;
import com.example.demo.Repository.GroupeRepository;
import com.example.demo.Repository.ModuleRepository;
import com.example.demo.Repository.NoteRepository;
import com.example.demo.client.NotificationClient;
import com.example.demo.client.UserClient;
import com.example.demo.dto.AbsenceRecordedRequestDTO;
import com.example.demo.dto.ClasseDTO;
import com.example.demo.dto.GroupeDTO;
import com.example.demo.dto.ModuleAssignedRequestDTO;
import com.example.demo.dto.NotePublishedRequestDTO;
import com.example.demo.dto.StudentProfileDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Absence;
import com.example.demo.entity.Classe;
import com.example.demo.entity.Groupe;
import com.example.demo.entity.Module;
import com.example.demo.entity.Note;

@Service
public class AcademicServiceImpl implements AcademicService {

    private final ModuleRepository moduleRepository;
    private final ClasseRepository classeRepository;
    private final GroupeRepository groupeRepository;
    private final NoteRepository noteRepository;
    private final AbsenceRepository absenceRepository;
    private final UserClient userClient;

    @Autowired
    private NotificationClient notificationClient;

    public AcademicServiceImpl(
            ModuleRepository moduleRepository,
            ClasseRepository classeRepository,
            GroupeRepository groupeRepository,
            NoteRepository noteRepository,
            AbsenceRepository absenceRepository,
            UserClient userClient) {
        this.moduleRepository  = moduleRepository;
        this.classeRepository  = classeRepository;
        this.groupeRepository  = groupeRepository;
        this.noteRepository    = noteRepository;
        this.absenceRepository = absenceRepository;
        this.userClient        = userClient;
    }

    // ===== HELPER =====
    private UserDTO getValidStudent(Long studentId) {
        try {
            UserDTO user = userClient.getUserById(studentId);
            if (user == null) {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Étudiant introuvable : id=" + studentId
                );
            }
            return user;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Étudiant introuvable : id=" + studentId
            );
        }
    }

    // ===== MODULE =====
    @Override
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    @Override
    public Module addModule(Module module) {
        Module saved = moduleRepository.save(module);

        if (saved.getTeacherId() != null) {
            try {
                UserDTO teacher = userClient.getUserById(saved.getTeacherId());
                Classe classe = classeRepository.findById(saved.getClasseId()).orElse(null);

                String teacherFullName = (teacher.getNom() != null ? teacher.getNom() : "")
                        + " " + (teacher.getPrenom() != null ? teacher.getPrenom() : "");

                ModuleAssignedRequestDTO dto = new ModuleAssignedRequestDTO(
                        teacher.getId(),
                        teacher.getEmail(),
                        teacherFullName.trim(),
                        (saved.getCode() != null ? saved.getCode() : "") + " - " + (saved.getNom() != null ? saved.getNom() : ""),
                        classe != null ? classe.getNom() : ""
                );

                notificationClient.notifyModuleAssigned(dto);

            } catch (Exception e) {
                System.err.println("⚠️ Notification module assigné échouée : " + e.getMessage());
            }
        }

        return saved;
    }

    @Override
    public Module updateModule(Long id, Module m) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found"));

        Long oldTeacherId = module.getTeacherId();

        module.setCode(m.getCode());
        module.setNom(m.getNom());
        module.setCoefficient(m.getCoefficient());
        module.setClasseId(m.getClasseId());
        module.setTeacherId(m.getTeacherId());
        module.setPartie(m.getPartie());

        Module saved = moduleRepository.save(module);

        // ✅ Notifier seulement si un NOUVEL enseignant a été assigné (ou changé)
        if (saved.getTeacherId() != null && !saved.getTeacherId().equals(oldTeacherId)) {
            try {
                UserDTO teacher = userClient.getUserById(saved.getTeacherId());
                Classe classe = classeRepository.findById(saved.getClasseId()).orElse(null);

                String teacherFullName = (teacher.getNom() != null ? teacher.getNom() : "")
                        + " " + (teacher.getPrenom() != null ? teacher.getPrenom() : "");

                ModuleAssignedRequestDTO dto = new ModuleAssignedRequestDTO(
                        teacher.getId(),
                        teacher.getEmail(),
                        teacherFullName.trim(),
                        (saved.getCode() != null ? saved.getCode() : "") + " - " + (saved.getNom() != null ? saved.getNom() : ""),
                        classe != null ? classe.getNom() : ""
                );

                notificationClient.notifyModuleAssigned(dto);

            } catch (Exception e) {
                System.err.println("⚠️ Notification module (update) échouée : " + e.getMessage());
            }
        }

        return saved;
    }

    @Override
    public void deleteModule(Long id) {
        moduleRepository.deleteById(id);
    }

    // ===== CLASSE =====
    @Override
    public List<Classe> getAllClasses() {
        return classeRepository.findAll();
    }

    @Override
    public Classe addClasse(Classe classe) {
        return classeRepository.save(classe);
    }

    @Override
    public Classe updateClasse(Long id, Classe c) {
        Classe classe = classeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Classe not found"));
        classe.setNom(c.getNom());
        classe.setNiveau(c.getNiveau());
        classe.setSpecialite(c.getSpecialite());
        return classeRepository.save(classe);
    }

    @Override
    public void deleteClasse(Long id) {
        classeRepository.deleteById(id);
    }

    // ===== GROUPE =====
    @Override
    public List<Groupe> getAllGroupes() {
        return groupeRepository.findAll();
    }

    @Override
    public Groupe addGroupe(Groupe groupe) {
        return groupeRepository.save(groupe);
    }

    @Override
    public Groupe updateGroupe(Long id, Groupe g) {
        Groupe groupe = groupeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Groupe not found"));
        groupe.setNom(g.getNom());
        groupe.setClasseId(g.getClasseId());
        return groupeRepository.save(groupe);
    }

    @Override
    public void deleteGroupe(Long id) {
        groupeRepository.deleteById(id);
    }

    // ===== NOTE =====
    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    public List<Note> getNotesByStudent(Long studentId) {
        return noteRepository.findByStudentId(studentId);
    }

    @Override
    public Note addNote(Note note) {
        UserDTO student = getValidStudent(note.getStudentId());

        double cc = note.getNoteCC() == null ? 0 : note.getNoteCC();
        double tp = note.getNoteTP() == null ? 0 : note.getNoteTP();
        double exam = note.getNoteExam() == null ? 0 : note.getNoteExam();
        note.setMoyenne((cc + tp + exam) / 3);

        Note saved = noteRepository.save(note);

        // ✅ Notification — ne bloque jamais la sauvegarde de la note
        try {
            Module mod = moduleRepository.findById(note.getModuleId()).orElse(null);

            String moduleLabel = (mod != null)
                    ? ((mod.getCode() != null ? mod.getCode() : "") + " - " + (mod.getNom() != null ? mod.getNom() : ""))
                    : "Module";

            String studentFullName = (student.getNom() != null ? student.getNom() : "")
                    + " " + (student.getPrenom() != null ? student.getPrenom() : "");

            NotePublishedRequestDTO dto = new NotePublishedRequestDTO(
                    student.getId(),
                    student.getEmail(),
                    studentFullName.trim(),
                    moduleLabel,
                    saved.getMoyenne() != null ? saved.getMoyenne() : 0.0
            );

            notificationClient.notifyNotePublished(dto);

        } catch (Exception e) {
            System.err.println("⚠️ Notification note échouée : " + e.getMessage());
        }

        return saved;
    }

    @Override
    public Note updateNote(Long id, Note n) {
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Note not found"));
        double cc   = n.getNoteCC()   == null ? 0 : n.getNoteCC();
        double tp   = n.getNoteTP()   == null ? 0 : n.getNoteTP();
        double exam = n.getNoteExam() == null ? 0 : n.getNoteExam();
        note.setNoteCC(cc);
        note.setNoteTP(tp);
        note.setNoteExam(exam);
        note.setMoyenne((cc + tp + exam) / 3);
        return noteRepository.save(note);
    }

    @Override
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    // ===== ABSENCE =====
    @Override
    public List<Absence> getAllAbsences() {
        return absenceRepository.findAll();
    }

    @Override
    public List<Absence> getAbsencesByStudent(Long studentId) {
        return absenceRepository.findByStudentId(studentId);
    }

    @Override
    public Absence addAbsence(Absence absence) {
        return absenceRepository.save(absence);
    }

    @Override
    public List<Absence> addAbsences(List<Absence> absences) {
        for (Absence a : absences) {
            getValidStudent(a.getStudentId());
        }
        List<Absence> saved = absenceRepository.saveAll(absences);

        for (Absence a : saved) {
            try {
                UserDTO student = userClient.getUserById(a.getStudentId());
                Module mod = moduleRepository.findById(a.getModuleId()).orElse(null);

                String moduleLabel = (mod != null)
                        ? ((mod.getCode() != null ? mod.getCode() : "") + " - " + (mod.getNom() != null ? mod.getNom() : ""))
                        : "Module";

                String studentFullName = (student.getNom() != null ? student.getNom() : "")
                        + " " + (student.getPrenom() != null ? student.getPrenom() : "");

                AbsenceRecordedRequestDTO dto = new AbsenceRecordedRequestDTO(
                        student.getId(),
                        student.getEmail(),
                        studentFullName.trim(),
                        moduleLabel,
                        a.getDate() != null ? a.getDate().toString() : "",
                        a.getHeure() != null ? a.getHeure() : ""
                );

                notificationClient.notifyAbsenceRecorded(dto);

            } catch (Exception e) {
                System.err.println("⚠️ Notification absence échouée pour studentId=" + a.getStudentId() + " : " + e.getMessage());
            }
        }

        return saved;
    }

    @Override
    public Absence updateAbsence(Long id, Absence a) {
        Absence absence = absenceRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Absence not found"));
        absence.setStudentId(a.getStudentId());
        absence.setModuleId(a.getModuleId());
        absence.setDate(a.getDate());
        absence.setJustifiee(a.isJustifiee());
        return absenceRepository.save(absence);
    }

    @Override
    public void deleteAbsence(Long id) {
        absenceRepository.deleteById(id);
    }

    // ===== TEACHER-SPECIFIC =====
    @Override
    public List<Module> getModulesByTeacher(Long teacherId) {
        return moduleRepository.findByTeacherId(teacherId);
    }

    @Override
    public List<Note> getNotesByModules(List<Long> moduleIds) {
        if (moduleIds == null || moduleIds.isEmpty()) return List.of();
        return noteRepository.findByModuleIdIn(moduleIds);
    }

    @Override
    public List<Absence> getAbsencesByModules(List<Long> moduleIds) {
        if (moduleIds == null || moduleIds.isEmpty()) return List.of();
        return absenceRepository.findByModuleIdIn(moduleIds);
    }

    // ===== STUDENT PROFILE =====
    @Override
    public StudentProfileDTO getStudentProfile(Long studentId) {
        UserDTO user = userClient.getUserById(studentId);

        StudentProfileDTO dto = new StudentProfileDTO();
        dto.setUser(user);

        if (user.getClasseId() != null) {
            classeRepository.findById(user.getClasseId()).ifPresent(c -> {
                ClasseDTO classeDTO = new ClasseDTO();
                classeDTO.setId(c.getId());
                classeDTO.setNom(c.getNom());
                classeDTO.setNiveau(c.getNiveau());
                classeDTO.setSpecialite(c.getSpecialite());
                dto.setClasse(classeDTO);
            });
        }

        if (user.getGroupeId() != null) {
            groupeRepository.findById(user.getGroupeId()).ifPresent(g -> {
                GroupeDTO groupeDTO = new GroupeDTO();
                groupeDTO.setId(g.getId());
                groupeDTO.setNom(g.getNom());
                groupeDTO.setClasseId(g.getClasseId());
                dto.setGroupe(groupeDTO);
            });
        }

        dto.setNotes(noteRepository.findByStudentId(studentId));
        dto.setAbsences(absenceRepository.findByStudentId(studentId));

        if (user.getClasseId() != null) {
            dto.setModules(moduleRepository.findByClasseId(user.getClasseId()));
        } else {
            dto.setModules(List.of());
        }

        return dto;
    }
}