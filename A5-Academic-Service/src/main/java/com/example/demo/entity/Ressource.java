package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity

public class Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Type de ressource : COURS | TD | CORRECTION
    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false, length = 255)
    private String titre;

    // Nom original du fichier (ex: "chapitre3.pdf")
    @Column(nullable = false, length = 255)
    private String fileName;

    // Chemin de stockage sur le disque
    @Column(nullable = false, length = 500)
    private String filePath;

    // Taille en octets, utilisée par le frontend pour l'affichage
    private Long tailleFichier;

    @Column(nullable = false)
    private LocalDate dateAjout = LocalDate.now();

    // ─── Relation vers Module (entité locale à ce microservice) ───
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    // ⚠️ Pas de relation JPA vers "User" : les utilisateurs vivent dans
    // A4-USER-SERVICE (microservice séparé), accessible via UserClient (Feign).
    // On stocke donc juste l'id, exactement comme Module.teacherId
    // ou Justification.studentId ailleurs dans ce projet.
    @Column(nullable = false)
    private Long teacherId;

    public Ressource() {}

    // ─── Getters / Setters ───
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public Long getTailleFichier() { return tailleFichier; }
    public void setTailleFichier(Long tailleFichier) { this.tailleFichier = tailleFichier; }

    public LocalDate getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDate dateAjout) { this.dateAjout = dateAjout; }

    public Module getModule() { return module; }
    public void setModule(Module module) { this.module = module; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
}