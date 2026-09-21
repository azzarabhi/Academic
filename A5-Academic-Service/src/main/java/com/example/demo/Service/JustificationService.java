package com.example.demo.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Justification;

public interface JustificationService {

    Justification submit(
            Long absenceId,
            Long studentId,
            MultipartFile file
    );

    List<Justification> getAll();

    List<Justification> getByStudent(Long studentId);

    Justification getById(Long id);

    Justification accept(Long id, String comment);

    Justification reject(Long id, String comment);

    // ✅ Nouveau : lire les bytes du fichier PDF pour le servir au front
    byte[] readFile(Justification justification) throws IOException;
}