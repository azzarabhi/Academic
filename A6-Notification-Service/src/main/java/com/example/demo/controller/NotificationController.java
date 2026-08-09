package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.*;
import com.example.demo.entity.Notification;
import com.example.demo.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // ================= HEALTH =================

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "Notification Service");
    }

    // ================= ENVOI =================

    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest req) {
        try {
            return ResponseEntity.ok(notificationService.sendEmail(req));
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    @PostMapping("/note-published")
    public ResponseEntity<?> notePublished(@RequestBody NotePublishedRequest req) {
        try {
            return ResponseEntity.ok(notificationService.notifyNotePublished(req));
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    @PostMapping("/absence-recorded")
    public ResponseEntity<?> absenceRecorded(@RequestBody AbsenceRecordedRequest req) {
        try {
            return ResponseEntity.ok(notificationService.notifyAbsenceRecorded(req));
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    @PostMapping("/ai-alert")
    public ResponseEntity<?> aiAlert(@RequestBody AiAlertRequest req) {
        try {
            return ResponseEntity.ok(notificationService.notifyAiAlert(req));
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    // ================= LECTURE =================

    @GetMapping
    public List<Notification> getAll() {
        return notificationService.getAll();
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getByUser(@PathVariable Long userId) {
        return notificationService.getByUser(userId);
    }

    @GetMapping("/user/{userId}/unread")
    public List<Notification> getUnread(@PathVariable Long userId) {
        return notificationService.getUnreadByUser(userId);
    }

    @GetMapping("/user/{userId}/unread/count")
    public Map<String, Long> countUnread(@PathVariable Long userId) {
        return Map.of("count", notificationService.countUnread(userId));
    }

    // ================= MISE À JOUR =================

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(notificationService.markAsRead(id));
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    @PutMapping("/user/{userId}/read-all")
    public Map<String, String> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return Map.of("status", "OK");
    }

    // ================= SUPPRESSION =================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            notificationService.delete(id);
            return ResponseEntity.ok(Map.of("status", "deleted", "id", id));
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    @DeleteMapping("/user/{userId}/all")
    public ResponseEntity<?> deleteAllByUser(@PathVariable Long userId) {
        try {
            notificationService.deleteAllByUser(userId);
            return ResponseEntity.ok(Map.of("status", "deleted", "userId", userId));
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    // ================= UTILS =================

    private ResponseEntity<?> errorResponse(Exception e) {
        System.err.println("❌ NotificationController: "
            + e.getClass().getSimpleName() + " - " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error",
                    e.getClass().getSimpleName() + ": " +
                    (e.getMessage() != null ? e.getMessage() : "erreur inconnue")));
    }
    
    
    
    @PostMapping("/module-assigned")
    public ResponseEntity<?> moduleAssigned(@RequestBody ModuleAssignedRequest req) {
        try {
            return ResponseEntity.ok(notificationService.notifyModuleAssigned(req));
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    @PostMapping("/new-student")
    public ResponseEntity<?> newStudent(@RequestBody NewStudentRequest req) {
        try {
            return ResponseEntity.ok(notificationService.notifyNewStudent(req));
        } catch (Exception e) {
            return errorResponse(e);
        }
    }
}