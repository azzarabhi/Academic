package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Ressource;

import java.util.List;

public interface RessourceRepository extends JpaRepository<Ressource, Long> {

    List<Ressource> findByModule_IdOrderByDateAjoutDesc(Long moduleId);

    // teacherId est un champ simple (pas une relation) → pas de "_"
    boolean existsByIdAndTeacherId(Long id, Long teacherId);
}