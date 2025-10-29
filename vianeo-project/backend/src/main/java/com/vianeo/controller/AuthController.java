package com.vianeo.controller;

import com.vianeo.dto.JwtResponse;
import com.vianeo.dto.LoginRequest;
import com.vianeo.dto.MessageResponse;
import com.vianeo.dto.UserRegistrationRequest;
import com.vianeo.entity.User;
import com.vianeo.repository.UserRepository;
import com.vianeo.security.JwtUtils;
import com.vianeo.security.UserPrincipal;
import com.vianeo.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    EmailService emailService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

        // Récupérer la string du rôle
        String roleStr = "";
        if (!userDetails.getAuthorities().isEmpty()) {
            roleStr = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        }
        // Convertir la string en enum User.Role (attention à la casse)
        User.Role roleEnum = User.Role.valueOf(roleStr);

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getNom(),
                userDetails.getPrenom(),
                roleEnum
        ));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Erreur: L'email est déjà utilisé!"));
        }

        // Créer le nouveau compte utilisateur
        User user = new User(signUpRequest.getNom(),
                signUpRequest.getPrenom(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        user.setTelephone(signUpRequest.getTelephone());
        user.setRole(signUpRequest.getRole());
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setActive(false); // L'utilisateur doit être activé par un admin

        userRepository.save(user);

        // Envoyer un email de vérification
        try {
            emailService.sendVerificationEmail(user);
        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer l'inscription
            System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
        }

        return ResponseEntity.ok(new MessageResponse("Utilisateur enregistré avec succès! En attente d'activation par un administrateur."));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Token de vérification invalide!"));
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Email vérifié avec succès!"));
    }
}