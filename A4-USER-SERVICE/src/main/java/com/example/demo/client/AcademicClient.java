package com.example.demo.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "A5-ACADEMIC-SERVICE")
public interface AcademicClient {

    @GetMapping("/academic/modules/classe/{classeId}/teachers")
    List<Long> getTeacherIdsByClasse(@PathVariable Long classeId);

    @GetMapping("/academic/classes/{id}/name")
    String getClasseName(@PathVariable Long id);

    @GetMapping("/academic/groupes/{id}/name")
    String getGroupeName(@PathVariable Long id);
    
    
 }