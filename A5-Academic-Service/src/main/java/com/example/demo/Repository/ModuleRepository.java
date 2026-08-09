package com.example.demo.Repository;
import com.example.demo.entity.Module;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
	 List<Module> findByTeacherId(Long teacherId);
	    List<Module> findByClasseId(Long classeId);
}