package com.vianeo.controller;

import com.vianeo.dto.auth.LoginRequest;
import com.vianeo.dto.auth.MeResponse;
import com.vianeo.dto.auth.TokenResponse;
import com.vianeo.model.entity.Compte;
import com.vianeo.repository.CompteRepository;
import com.vianeo.security.CompteDetails;
import com.vianeo.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private CompteRepository compteRepository;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // l’UI envoie dans "username" un identifiant qui peut être un username OU un email
        String ident = loginRequest.getUsername();

        // 1) Résoudre l’identifiant en vrai username
        var compteOpt = compteRepository.findByUsername(ident)
                .or(() -> compteRepository.findByEmail(ident));
        if (compteOpt.isEmpty()) {
            // éviter de leaker si c’est username ou email qui est faux
            return ResponseEntity.status(401).build();
        }
        String realUsername = compteOpt.get().getUsername();

        // 2) Authentifier avec (username réel, password)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        realUsername,
                        loginRequest.getPassword()
                )
        );

        CompteDetails compteDetails = (CompteDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(compteDetails);

        // 3) MAJ lastLogin
        Compte compte = compteDetails.getCompte();
        compte.setLastLogin(LocalDateTime.now());
        compteRepository.save(compte);

        TokenResponse tokenResponse = new TokenResponse(accessToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CompteDetails cd)) {
            return ResponseEntity.status(401).build();
        }

        Compte c = cd.getCompte();

        // Normalise le rôle: "ROLE_ADMIN" -> "ADMIN"
        String role = cd.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse("ROLE_CHEF_CHANTIER")
                .replaceFirst("^ROLE_", "");

        // Récupère l'utilisateur (anciennement "personnel")
        var u = c.getUtilisateur(); // <<< ICI (au lieu de getPersonnel())

        String nom = "";
        String prenom = "";
        if (u != null) {
            nom = u.getNom() != null ? u.getNom() : "";
            prenom = u.getPrenom() != null ? u.getPrenom() : "";
        }

        MeResponse resp = new MeResponse(
                c.getId(),
                nom,
                prenom,
                c.getEmail(),
                role // "ADMIN" ou "CHEF_CHANTIER"
        );
        return ResponseEntity.ok(resp);
    }


}