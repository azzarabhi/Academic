package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public String sendEmail(String to, String subject, String body) {
        if (to == null || to.isBlank()) {
            return "Adresse email destinataire manquante";
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("noreply@academia.tn");
            mailSender.send(message);
            return null;
        } catch (Exception e) {
            System.err.println("Erreur envoi email vers " + to + " : " + e.getMessage());
            return e.getMessage();
        }
    }
}