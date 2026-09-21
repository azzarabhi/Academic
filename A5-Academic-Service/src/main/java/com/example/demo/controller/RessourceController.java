package com.example.demo.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Service.RessourceService;
import com.example.demo.dto.RessourceDTO;
import com.example.demo.entity.Ressource;

import java.util.List;

@RestController
@RequestMapping("/academic/ressources")
// Pas de @CrossOrigin ici : CorsConfig.java gère déjà le CORS globalement.
// L'ajouter en double avec allowCredentials(true) dans CorsConfig peut
// provoquer une erreur au runtime ("allowedOrigins cannot contain the
// special value *" quand credentials=true).
public class RessourceController {

    private final RessourceService ressourceService;

    public RessourceController(RessourceService ressourceService) {
        this.ressourceService = ressourceService;
    }

    // GET /api/ressources/module/{moduleId}  — accessible teacher + student
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<RessourceDTO>> getByModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(ressourceService.getByModule(moduleId));
    }

    // POST /api/ressources  (multipart/form-data) — teacher uniquement
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("moduleId") Long moduleId,
            @RequestParam("type") String type,
            @RequestParam(value = "titre", required = false) String titre,
            @RequestParam("teacherId") Long teacherId
    ) {
        try {
            RessourceDTO dto = ressourceService.upload(file, moduleId, type, titre, teacherId);
            return ResponseEntity.ok(dto);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /api/ressources/{id}?teacherId=...  — teacher uniquement, auteur seulement
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long teacherId) {
        try {
            ressourceService.delete(id, teacherId);
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // GET /api/ressources/{id}/file — accessible teacher + student
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        Ressource ressource = ressourceService.getEntity(id);
        byte[] content = ressourceService.getFileContent(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + ressource.getFileName() + "\"")
                .body(content);
    }
}