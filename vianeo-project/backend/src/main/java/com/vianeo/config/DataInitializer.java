package com.vianeo.config;

import com.vianeo.entity.User;
import com.vianeo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        // Créer un utilisateur admin par défaut s'il n'existe pas
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setNom("Admin");
            admin.setPrenom("Vianeo");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(User.Role.ADMIN);
            admin.setActive(true);
            admin.setEmailVerified(true);
            
            userRepository.save(admin);
            System.out.println("Utilisateur admin créé avec l'email: " + adminEmail);
        }
    }
}