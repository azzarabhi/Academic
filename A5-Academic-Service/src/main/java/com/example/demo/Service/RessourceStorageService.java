package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
 
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
 
@Service
public class RessourceStorageService {
 
    // Ajoutez dans application.properties :
    // ressources.storage.path=uploads/ressources
    @Value("${ressources.storage.path:uploads/ressources}")
    private String storagePath;
 
    /**
     * Sauvegarde le fichier PDF sur le disque et retourne le chemin relatif stocké en base.
     */
    public String store(MultipartFile file) {
        try {
            Path dir = Paths.get(storagePath);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
 
            String extension = ".pdf";
            String uniqueName = UUID.randomUUID() + extension;
            Path target = dir.resolve(uniqueName);
 
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
 
            return target.toString();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier : " + e.getMessage(), e);
        }
    }
 
    /**
     * Charge le contenu du fichier pour le renvoyer au client (visualisation / téléchargement).
     */
    public byte[] load(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Fichier introuvable : " + e.getMessage(), e);
        }
    }
 
    /**
     * Supprime le fichier physique du disque.
     */
    public void delete(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            // On log sans bloquer la suppression en base — le fichier orphelin
            // peut être nettoyé plus tard si besoin.
            System.err.println("Impossible de supprimer le fichier : " + filePath);
        }
    }
}