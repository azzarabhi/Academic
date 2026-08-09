package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.*;
import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    // ================= ENVOI =================

    public Notification sendEmail(EmailRequest req) {
        Notification n = new Notification(req.getUserId(), "GENERAL",
                safe(req.getSubject()), safe(req.getBody()), "EMAIL");
        String error = emailService.sendEmail(req.getTo(), req.getSubject(), req.getBody());
        n.setSent(error == null);
        n.setErrorMessage(error);
        return notificationRepository.save(n);
    }

    public Notification notifyNotePublished(NotePublishedRequest req) {
        String moduleName  = req.getModuleName()  != null ? req.getModuleName()  : "Module";
        String studentName = req.getStudentName() != null ? req.getStudentName() : "Étudiant";
        double moyenne     = req.getMoyenne()     != null ? req.getMoyenne()     : 0.0;

        String subject = "Nouvelle note publiée — " + moduleName;
        String body = String.format(
            "Bonjour %s,\n\nUne nouvelle note vient d'être publiée pour le module %s.\n" +
            "Moyenne obtenue : %.2f/20\n\n" +
            "Connectez-vous à votre espace AcadémIA pour plus de détails.",
            studentName, moduleName, moyenne
        );

        Notification n = new Notification(req.getStudentId(), "NOTE_PUBLISHED", subject, body, "EMAIL");
        String error;
        try {
            error = emailService.sendEmail(req.getStudentEmail(), subject, body);
        } catch (Exception e) {
            error = e.getMessage();
        }
        n.setSent(error == null);
        n.setErrorMessage(error);
        return notificationRepository.save(n);
    }

    public Notification notifyAbsenceRecorded(AbsenceRecordedRequest req) {
        String moduleName  = req.getModuleName()  != null ? req.getModuleName()  : "Module";
        String studentName = req.getStudentName() != null ? req.getStudentName() : "Étudiant";
        String date        = req.getDate()        != null ? req.getDate()        : "";
        String heure       = req.getHeure()       != null ? req.getHeure()       : "";

        String subject = "📅 Absence enregistrée  — " + moduleName;
        String body = String.format(
            "Bonjour %s,\n\nUne absence a été enregistrée pour le module %s le %s à %s.\n\n" +
            "Si cette absence est injustifiée, veuillez régulariser votre situation auprès de l'administration.",
            studentName, moduleName, date, heure
        );

        Notification n = new Notification(req.getStudentId(), "ABSENCE_RECORDED", subject, body, "EMAIL");
        String error;
        try {
            error = emailService.sendEmail(req.getStudentEmail(), subject, body);
        } catch (Exception e) {
            error = e.getMessage();
        }
        n.setSent(error == null);
        n.setErrorMessage(error);
        return notificationRepository.save(n);
    }

    public Notification notifyAiAlert(AiAlertRequest req) {
        String riskLevel   = req.getRiskLevel()   != null ? req.getRiskLevel()   : "inconnu";
        String studentName = req.getStudentName() != null ? req.getStudentName() : "Étudiant";
        String message     = req.getMessage()     != null ? req.getMessage()     : "";

        String subject = "Alerte académique — Niveau de risque : " + riskLevel;
        String body = String.format(
            "Bonjour,\n\nL'analyse académique a détecté un niveau de risque \"%s\" " +
            "pour l'étudiant %s.\n\n%s",
            riskLevel, studentName, message
        );

        Notification n = new Notification(req.getStudentId(), "AI_ALERT", subject, body, "EMAIL");
        String error;
        try {
            error = emailService.sendEmail(req.getStudentEmail(), subject, body);
        } catch (Exception e) {
            error = e.getMessage();
        }
        n.setSent(error == null);
        n.setErrorMessage(error);
        return notificationRepository.save(n);
    }

    // ================= LECTURE / GESTION =================

    public List<Notification> getByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadByUser(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    public long countUnread(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    public List<Notification> getAll() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }

    public Notification markAsRead(Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification introuvable : " + id));
        n.setRead(true);
        return notificationRepository.save(n);
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unread =
            notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    // ================= SUPPRESSION =================

    public void delete(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification introuvable : " + id);
        }
        notificationRepository.deleteById(id);
    }

    public void deleteAllByUser(Long userId) {
        List<Notification> all = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        notificationRepository.deleteAll(all);
    }

    // ================= UTILS =================

    private String safe(String s) {
        return s != null ? s : "";
    }
    
    
    public Notification notifyModuleAssigned(ModuleAssignedRequest req) {
        String moduleName = req.getModuleName() != null ? req.getModuleName() : "Module";
        String classeName = req.getClasseName() != null ? req.getClasseName() : "";
        String teacherName = req.getTeacherName() != null ? req.getTeacherName() : "Enseignant";

        String subject = "Nouveau module assigné — " + moduleName;
        String body = String.format(
                "Bonjour %s,\n\nUn nouveau module vous a été assigné : %s (classe %s).\n\nConnectez-vous à votre espace AcadémIA pour plus de détails.",
                teacherName, moduleName, classeName
        );

        Notification n = new Notification(req.getTeacherId(), "MODULE_ASSIGNED", subject, body, "EMAIL");
        String error;
        try {
            error = emailService.sendEmail(req.getTeacherEmail(), subject, body);
        } catch (Exception e) {
            error = e.getMessage();
        }
        n.setSent(error == null);
        n.setErrorMessage(error);
        return notificationRepository.save(n);
    }

    public Notification notifyNewStudent(NewStudentRequest req) {
        String studentName = req.getStudentName() != null ? req.getStudentName() : "Étudiant";
        String classeName = req.getClasseName() != null ? req.getClasseName() : "";
        String groupeName = req.getGroupeName() != null ? req.getGroupeName() : "";
        String teacherName = req.getTeacherName() != null ? req.getTeacherName() : "Enseignant";

        String subject = "Nouvel étudiant — " + classeName;
        String body = String.format(
                "Bonjour %s,\n\nUn nouvel étudiant a été ajouté à votre classe : %s (%s - %s).\n\nConnectez-vous à votre espace AcadémIA pour plus de détails.",
                teacherName, studentName, classeName, groupeName
        );

        Notification n = new Notification(req.getTeacherId(), "NEW_STUDENT", subject, body, "EMAIL");
        String error;
        try {
            error = emailService.sendEmail(req.getTeacherEmail(), subject, body);
        } catch (Exception e) {
            error = e.getMessage();
        }
        n.setSent(error == null);
        n.setErrorMessage(error);
        return notificationRepository.save(n);
    }
}