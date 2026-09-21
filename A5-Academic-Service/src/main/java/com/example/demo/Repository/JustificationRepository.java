package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Justification;

@Repository
public interface JustificationRepository
        extends JpaRepository<Justification, Long> {

    Optional<Justification> findByAbsenceId(Long absenceId);

    List<Justification> findByStudentId(Long studentId);

    List<Justification> findByStatus(String status);
    boolean existsById(Long id);
}