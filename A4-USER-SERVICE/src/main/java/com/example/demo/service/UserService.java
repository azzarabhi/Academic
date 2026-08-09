package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.Repository.UserRepository;
import com.example.demo.client.AcademicClient;
import com.example.demo.client.NotificationClient;
import com.example.demo.dto.AssignStudentDTO;
import com.example.demo.dto.AuthUserResponse;
import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.NewStudentRequestDTO;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFoundException;

@Service
public class UserService {

	
	@Autowired
    private AcademicClient academicClient;

    @Autowired
    private NotificationClient notificationClient;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder encoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    // ================= REGISTER / VERIFY =================

    public User registerUser(CreateUserRequest request) {
        User u = new User();
        u.setNom(request.getNom());
        u.setPrenom(request.getPrenom());
        u.setCin(request.getCin());
        u.setEmail(request.getEmail());
        u.setPassword(encoder.encode(request.getPassword()));
        u.setRole(Role.STUDENT);
        u.setEnabled(false);

        String token = UUID.randomUUID().toString();
        u.setVerificationToken(token);

        userRepository.save(u);
        emailService.sendVerificationMail(u.getEmail(), token);

        return u;
    }

    public void verifyAccount(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new UserNotFoundException("Token invalide"));

        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    // ================= CRUD =================

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());

        return userRepository.save(user);
    }
 // Ajouter cette méthode
    public User getUserByCinEntity(String cin) {
        return userRepository.findByCin(cin)
                .orElseThrow(() -> new UserNotFoundException("User introuvable"));
    }
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    // ================= ROLE (SUPER_ADMIN) =================

    public void updateRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setRole(role);
        userRepository.save(user);
    }

    // ================= ASSIGN STUDENT (ADMIN) =================

    public User assignStudent(Long id, AssignStudentDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getRole().equals(Role.STUDENT) && !user.getRole().equals(Role.TEACHER)) {
            throw new IllegalStateException("Seuls STUDENT et TEACHER peuvent être assignés");
        }

        user.setClasseId(dto.getClasseId());
        user.setGroupeId(dto.getGroupeId());
        User saved = userRepository.save(user);

        // ✅ Notifier les enseignants de cette classe SEULEMENT si c'est un STUDENT
        if (saved.getRole().equals(Role.STUDENT) && dto.getClasseId() != null) {
            try {
                List<Long> teacherIds = academicClient.getTeacherIdsByClasse(dto.getClasseId());
                String classeName = academicClient.getClasseName(dto.getClasseId());
                String groupeName = dto.getGroupeId() != null ? academicClient.getGroupeName(dto.getGroupeId()) : "";
                String studentFullName = saved.getNom() + " " + saved.getPrenom();

                for (Long teacherId : teacherIds) {
                    try {
                        User teacher = userRepository.findById(teacherId).orElse(null);
                        if (teacher == null) continue;

                        NewStudentRequestDTO notifDto = new NewStudentRequestDTO(
                                teacher.getId(),
                                teacher.getEmail(),
                                teacher.getNom() + " " + teacher.getPrenom(),
                                studentFullName,
                                classeName,
                                groupeName
                        );
                        notificationClient.notifyNewStudent(notifDto);

                    } catch (Exception e) {
                        System.err.println("⚠️ Notification new student échouée pour teacherId=" + teacherId + " : " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ Récupération des enseignants de la classe échouée : " + e.getMessage());
            }
        }

        return saved;
    }
    // ================= LOOKUP =================

    public AuthUserResponse getUserByCin(String cin) {
        User user = userRepository.findByCin(cin)
                .orElseThrow(() -> new UserNotFoundException("User introuvable"));

        AuthUserResponse dto = new AuthUserResponse();
        dto.setId(user.getId());
        dto.setCin(user.getCin());
        dto.setPassword(user.getPassword());
        dto.setRoles(Set.of(user.getRole().name()));

        return dto;
    }
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Email introuvable"));
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);
        emailService.sendResetPasswordMail(email, token);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
            .orElseThrow(() -> new RuntimeException("Token invalide"));
        if (user.getResetTokenExpiry() == null ||
            user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expiré");
        }
        user.setPassword(encoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }
    
    
    public User updatePhoto(Long id, String photoUrl) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPhotoUrl(photoUrl);
        return userRepository.save(user);
    }
}