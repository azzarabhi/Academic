package com.example.demo.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Repository.ModuleRepository;
import com.example.demo.Repository.RessourceRepository;
import com.example.demo.dto.RessourceDTO;
import com.example.demo.entity.Module;
import com.example.demo.entity.Ressource;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RessourceService {

    private static final long MAX_SIZE = 20L * 1024 * 1024; // 20 Mo, en phase avec le frontend

    private final RessourceRepository ressourceRepository;
    private final RessourceStorageService storageService;
    private final ModuleRepository moduleRepository;
    // ⚠️ Pas de UserRepository : les users sont dans A4-USER-SERVICE (Feign),
    // pas une table locale de ce microservice.

    public RessourceService(RessourceRepository ressourceRepository,
                             RessourceStorageService storageService,
                             ModuleRepository moduleRepository) {
        this.ressourceRepository = ressourceRepository;
        this.storageService      = storageService;
        this.moduleRepository    = moduleRepository;
    }

    public List<RessourceDTO> getByModule(Long moduleId) {
        return ressourceRepository.findByModule_IdOrderByDateAjoutDesc(moduleId)
                .stream()
                .map(RessourceDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * @param teacherId id de l'enseignant, envoyé par le frontend (comme pour
     *                  getModulesByTeacher, getNotesByTeacher, etc.)
     */
    public RessourceDTO upload(MultipartFile file, Long moduleId, String type, String titre, Long teacherId) {

        validateFile(file);
        validateType(type);

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Module introuvable"));

        // Sécurité : seul l'enseignant assigné au module peut y publier une ressource.
        if (!module.getTeacherId().equals(teacherId)) {
            throw new SecurityException("Vous n'êtes pas autorisé à publier sur ce module");
        }

        String storedPath = storageService.store(file);

        Ressource ressource = new Ressource();
        ressource.setType(type);
        ressource.setTitre(titre != null && !titre.isBlank() ? titre : file.getOriginalFilename());
        ressource.setFileName(file.getOriginalFilename());
        ressource.setFilePath(storedPath);
        ressource.setTailleFichier(file.getSize());
        ressource.setModule(module);
        ressource.setTeacherId(teacherId);

        Ressource saved = ressourceRepository.save(ressource);
        return RessourceDTO.fromEntity(saved);
    }

    public void delete(Long ressourceId, Long teacherId) {
        Ressource ressource = ressourceRepository.findById(ressourceId)
                .orElseThrow(() -> new IllegalArgumentException("Ressource introuvable"));

        if (!ressource.getTeacherId().equals(teacherId)) {
            throw new SecurityException("Vous n'êtes pas autorisé à supprimer cette ressource");
        }

        storageService.delete(ressource.getFilePath());
        ressourceRepository.delete(ressource);
    }

    public byte[] getFileContent(Long ressourceId) {
        Ressource ressource = ressourceRepository.findById(ressourceId)
                .orElseThrow(() -> new IllegalArgumentException("Ressource introuvable"));
        return storageService.load(ressource.getFilePath());
    }

    public Ressource getEntity(Long ressourceId) {
        return ressourceRepository.findById(ressourceId)
                .orElseThrow(() -> new IllegalArgumentException("Ressource introuvable"));
    }

    // ─── Validations ───
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Fichier manquant");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("Fichier trop volumineux (max 20 Mo)");
        }
        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Seuls les fichiers PDF sont acceptés");
        }
    }

    private void validateType(String type) {
        if (!List.of("COURS", "TD", "CORRECTION").contains(type)) {
            throw new IllegalArgumentException("Type de ressource invalide");
        }
    }
}