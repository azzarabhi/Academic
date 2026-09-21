package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.entity.Ressource;

public class RessourceDTO {

    private Long id;
    private String type;
    private String titre;
    private String fileName;
    private Long tailleFichier;
    private LocalDate dateAjout;
    private Long moduleId;
    private Long teacherId;

    public RessourceDTO() {}

    public static RessourceDTO fromEntity(Ressource r) {
        RessourceDTO dto = new RessourceDTO();
        dto.id            = r.getId();
        dto.type          = r.getType();
        dto.titre         = r.getTitre();
        dto.fileName      = r.getFileName();
        dto.tailleFichier = r.getTailleFichier();
        dto.dateAjout     = r.getDateAjout();
        dto.moduleId      = r.getModule().getId();
        dto.teacherId     = r.getTeacherId();   // plus de r.getTeacher().getId()
        return dto;
    }

    // ─── Getters / Setters ───
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Long getTailleFichier() { return tailleFichier; }
    public void setTailleFichier(Long tailleFichier) { this.tailleFichier = tailleFichier; }

    public LocalDate getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDate dateAjout) { this.dateAjout = dateAjout; }

    public Long getModuleId() { return moduleId; }
    public void setModuleId(Long moduleId) { this.moduleId = moduleId; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
}