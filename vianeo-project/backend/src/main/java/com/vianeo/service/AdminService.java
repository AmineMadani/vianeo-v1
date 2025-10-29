package com.vianeo.service;

import com.vianeo.dto.UserRegistrationRequest;
import com.vianeo.entity.Chantier;
import com.vianeo.entity.RapportChantier;
import com.vianeo.entity.User;
import com.vianeo.repository.ChantierRepository;
import com.vianeo.repository.RapportChantierRepository;
import com.vianeo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChantierRepository chantierRepository;

    @Autowired
    private RapportChantierRepository rapportRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Statistiques utilisateurs
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countActiveUsersByRole(User.Role.CHEF_CHANTIER);
        long admins = userRepository.countActiveUsersByRole(User.Role.ADMIN);
        
        // Statistiques chantiers
        long totalChantiers = chantierRepository.count();
        long chantiersEnCours = chantierRepository.countByStatut(Chantier.Statut.EN_COURS);
        long chantiersTermines = chantierRepository.countByStatut(Chantier.Statut.TERMINE);
        
        // Statistiques rapports
        long totalRapports = rapportRepository.count();
        long rapportsBrouillon = rapportRepository.countByStatut(RapportChantier.Statut.BROUILLON);
        long rapportsValides = rapportRepository.countByStatut(RapportChantier.Statut.VALIDE);
        
        // Rapports de la semaine
        LocalDate debutSemaine = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
        LocalDate finSemaine = debutSemaine.plusDays(6);
        long rapportsSemaine = rapportRepository.findByDateBetween(debutSemaine, finSemaine).size();
        
        dashboard.put("users", Map.of(
            "total", totalUsers,
            "active", activeUsers,
            "admins", admins
        ));
        
        dashboard.put("chantiers", Map.of(
            "total", totalChantiers,
            "enCours", chantiersEnCours,
            "termines", chantiersTermines
        ));
        
        dashboard.put("rapports", Map.of(
            "total", totalRapports,
            "brouillon", rapportsBrouillon,
            "valides", rapportsValides,
            "semaine", rapportsSemaine
        ));
        
        return dashboard;
    }

    public User inviteUser(UserRegistrationRequest inviteRequest) {
        if (userRepository.existsByEmail(inviteRequest.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        // Générer un mot de passe temporaire
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        
        User user = new User(
            inviteRequest.getNom(),
            inviteRequest.getPrenom(),
            inviteRequest.getEmail(),
            passwordEncoder.encode(tempPassword)
        );
        
        user.setTelephone(inviteRequest.getTelephone());
        user.setRole(inviteRequest.getRole());
        user.setActive(true);
        user.setEmailVerified(false);
        user.setVerificationToken(UUID.randomUUID().toString());
        
        user = userRepository.save(user);
        
        // Envoyer l'email d'invitation avec le mot de passe temporaire
        try {
            emailService.sendInvitationEmail(user, tempPassword);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'invitation: " + e.getMessage());
        }
        
        return user;
    }
}