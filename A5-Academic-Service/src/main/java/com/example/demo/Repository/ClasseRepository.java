package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Classe;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {
}