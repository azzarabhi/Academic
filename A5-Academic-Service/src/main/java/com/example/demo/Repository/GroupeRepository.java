package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Groupe;

@Repository
public interface GroupeRepository
        extends JpaRepository<Groupe, Long> {
}