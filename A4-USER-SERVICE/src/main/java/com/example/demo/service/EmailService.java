package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender sender;

public void sendVerificationMail(String to, String token) {
        
        System.out.println("=== SENDING MAIL ===");
        System.out.println("TO: " + to);
        System.out.println("TOKEN: " + token);
        
        String link = "http://localhost:4200/verify?token=" + token;

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Vérification du compte");
        msg.setText("Cliquez ici pour activer votre compte : " + link);

        try {
            sender.send(msg);
            System.out.println("=== MAIL SENT OK ===");
        } catch (Exception e) {
            System.out.println("=== MAIL ERROR ===");
            e.printStackTrace();
        }
    }
public void sendResetPasswordMail(String to, String token) {
    String link = "http://localhost:4200/reset-password?token=" + token;
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo(to);
    msg.setSubject("Réinitialisation du mot de passe — AcadémIA");
    msg.setText("Cliquez sur ce lien pour réinitialiser votre mot de passe :\n\n"
        + link + "\n\nCe lien expire dans 30 minutes.");
    try {
        sender.send(msg);
        System.out.println("=== RESET MAIL SENT ===");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}