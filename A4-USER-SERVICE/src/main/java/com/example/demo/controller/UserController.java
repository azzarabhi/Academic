package com.example.demo.controller;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AssignStudentDTO;
import com.example.demo.dto.AuthUserResponse;
import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.RoleUpdateRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.entity.User;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    // ================= AUTH / REGISTER (public) =================

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Mail de vérification envoyé"
        ));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token) {
        userService.verifyAccount(token);
        return ResponseEntity.ok("Compte activé avec succès");
    }

    // ================= DEBUG =================

    @GetMapping("/test-role")
    public String testRole(Authentication authentication) {
        return authentication.getAuthorities().toString();
    }

    // ================= CRUD =================

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','TEACHER')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','TEACHER','STUDENT')")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','STUDENT','TEACHER')")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request,
            Authentication authentication) {

        // Un STUDENT ne peut modifier QUE son propre profil
        String role = authentication.getAuthorities().stream()
                .findFirst().map(a -> a.getAuthority()).orElse("");

        if (role.equals("ROLE_STUDENT") || role.equals("ROLE_TEACHER")) {
            // Récupérer l'ID de l'utilisateur connecté via son CIN depuis le token
            String cin = authentication.getName(); // le principal = CIN (selon ton AuthService)
            User connectedUser = userService.getUserByCinEntity(cin);
            if (!connectedUser.getId().equals(id)) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "Vous ne pouvez modifier que votre propre profil"));
            }
            // STUDENT ne peut modifier que nom/prenom/email — pas le rôle ni CIN
        }

        User updated = userService.updateUser(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    // ================= ROLE (SUPER_ADMIN uniquement) =================

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody RoleUpdateRequest dto) {
        userService.updateRole(id, dto.getRole());
        return ResponseEntity.ok("Role modifié");
    }

    // ================= ASSIGN CLASSE/GROUPE (ADMIN) =================

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public User assignStudent(@PathVariable Long id, @RequestBody AssignStudentDTO dto) {
        return userService.assignStudent(id, dto);
    }

    // ================= LOOKUP (public, mel login) =================

    @GetMapping("/cin/{cin}")
    public AuthUserResponse getUserByCin(@PathVariable String cin) {
        return userService.getUserByCin(cin);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        userService.forgotPassword(body.get("email"));
        return ResponseEntity.ok(Map.of("message", "Email de réinitialisation envoyé"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        userService.resetPassword(body.get("token"), body.get("password"));
        return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
    }
    
 // ================= PHOTO PROFIL =================
    @PostMapping("/{id}/photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Fichier vide"));
        }

        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg")
                || contentType.equals("image/png") || contentType.equals("image/webp"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "Format non supporté"));
        }

        try {
            String uploadDir = "uploads/photos";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String ext = contentType.equals("image/png") ? ".png"
                    : contentType.equals("image/webp") ? ".webp" : ".jpg";
            String filename = "user_" + id + "_" + UUID.randomUUID() + ext;
            Path filePath = Paths.get(uploadDir, filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String photoUrl = "/uploads/photos/" + filename;
            User updated = userService.updatePhoto(id, photoUrl);

            return ResponseEntity.ok(updated);  // ✅ يرجع User كامل (فيه photoUrl الجديد)

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur upload"));
        }
    }
}