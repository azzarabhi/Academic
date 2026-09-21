package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Service.JustificationService;
import com.example.demo.entity.Justification;

@RestController
@RequestMapping("/academic/justifications")
public class JustificationController {

    private final JustificationService service;

    public JustificationController(JustificationService service) {
        this.service = service;
    }

    @PostMapping(
            value = "/submit",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Justification> submit(
            @RequestParam Long absenceId,
            @RequestParam Long studentId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(
                service.submit(absenceId, studentId, file)
        );
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Justification>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(service.getByStudent(studentId));
    }

    @GetMapping
    public ResponseEntity<List<Justification>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Justification> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ✅ Nouveau : télécharger/afficher le PDF
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) throws IOException {
        Justification justification = service.getById(id);
        byte[] content = service.readFile(justification);
        
        String fileName = justification.getFileName() != null 
                ? justification.getFileName() 
                : "justification.pdf";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline()
                .filename(fileName)
                .build());
        headers.setContentLength(content.length);
        
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Justification> accept(
            @PathVariable Long id,
            @RequestParam(required = false) String comment) {
        return ResponseEntity.ok(service.accept(id, comment));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Justification> reject(
            @PathVariable Long id,
            @RequestParam(required = false) String comment) {
        return ResponseEntity.ok(service.reject(id, comment));
    }
}