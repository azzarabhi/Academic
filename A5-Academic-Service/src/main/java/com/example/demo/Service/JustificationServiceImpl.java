package com.example.demo.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Repository.AbsenceRepository;
import com.example.demo.Repository.JustificationRepository;
import com.example.demo.entity.Absence;
import com.example.demo.entity.Justification;

@Service
public class JustificationServiceImpl implements JustificationService {

    private final JustificationRepository justificationRepository;
    private final AbsenceRepository absenceRepository;

    private final Path uploadDirectory =
            Paths.get(System.getProperty("user.dir"), "uploads", "justifications");

    public JustificationServiceImpl(
            JustificationRepository justificationRepository,
            AbsenceRepository absenceRepository) {

        this.justificationRepository = justificationRepository;
        this.absenceRepository = absenceRepository;

        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Impossible de créer le dossier uploads",
                    e
            );
        }
    }

    @Override
    public Justification submit(
            Long absenceId,
            Long studentId,
            MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le fichier PDF est obligatoire"
            );
        }

        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Seuls les fichiers PDF sont acceptés"
            );
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le fichier ne doit pas dépasser 5 MB"
            );
        }

        Absence absence = absenceRepository.findById(absenceId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Absence introuvable"
                        ));

        if (!absence.getStudentId().equals(studentId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Cette absence n'appartient pas à cet étudiant"
            );
        }

        Justification justification =
                justificationRepository.findByAbsenceId(absenceId).orElse(null);

        if (justification != null && "ACCEPTEE".equals(justification.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Cette absence a déjà été justifiée et acceptée, modification impossible"
            );
        }

        String originalName = file.getOriginalFilename();

        String safeName = originalName == null
                ? "justification.pdf"
                : Paths.get(originalName)
                        .getFileName()
                        .toString();

        String fileName =
                UUID.randomUUID() + "_" + safeName;

        Path filePath =
                uploadDirectory.resolve(fileName);

        try {
            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de l'enregistrement du fichier"
            );
        }

        if (justification != null && justification.getFileUrl() != null) {
            try {
                String oldName = Paths.get(justification.getFileUrl()).getFileName().toString();
                Files.deleteIfExists(uploadDirectory.resolve(oldName));
            } catch (IOException ignored) {
            }
        }

        if (justification == null) {
            justification = new Justification();
        }

        justification.setAbsenceId(absenceId);
        justification.setStudentId(studentId);
        justification.setFileName(safeName);
        justification.setFileUrl(
                "/uploads/justifications/" + fileName
        );
        justification.setStatus("EN_ATTENTE");
        justification.setAdminComment(null);

        Justification saved = justificationRepository.save(justification);

        // ✅ Lors d'une nouvelle soumission, l'absence redevient NON justifiée
        absence.setJustifiee(false);
        absenceRepository.save(absence);

        return saved;
    }

    @Override
    public List<Justification> getAll() {
        return justificationRepository.findAll();
    }

    @Override
    public List<Justification> getByStudent(Long studentId) {
        return justificationRepository.findByStudentId(studentId);
    }

    @Override
    public Justification getById(Long id) {
        return justificationRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Justification introuvable"
                        ));
    }

    @Override
    public Justification accept(Long id, String comment) {

        Justification justification = getById(id);

        justification.setStatus("ACCEPTEE");
        justification.setAdminComment(comment);

        Justification saved = justificationRepository.save(justification);

        Absence absence = absenceRepository.findById(
                justification.getAbsenceId()
        ).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Absence introuvable"
                ));

        absence.setJustifiee(true);
        absenceRepository.save(absence);

        return saved;
    }

    @Override
    public Justification reject(Long id, String comment) {

        Justification justification = getById(id);

        justification.setStatus("REFUSEE");
        justification.setAdminComment(comment);

        Justification saved = justificationRepository.save(justification);

        // ✅ CRITIQUE : remettre l'absence à NON justifiée
        Absence absence = absenceRepository.findById(
                justification.getAbsenceId()
        ).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Absence introuvable"
                ));

        absence.setJustifiee(false);
        absenceRepository.save(absence);

        return saved;
    }

    @Override
    public byte[] readFile(Justification justification) throws IOException {
        String storedName = Paths.get(justification.getFileUrl()).getFileName().toString();
        Path filePath = uploadDirectory.resolve(storedName);
        if (!Files.exists(filePath)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Fichier introuvable sur le serveur"
            );
        }
        return Files.readAllBytes(filePath);
    }
}