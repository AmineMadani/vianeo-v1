package com.vianeo.controller;

import com.vianeo.dto.FournisseurDTO;
import com.vianeo.entity.Fournisseur;
import com.vianeo.service.FournisseurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurController {

    @Autowired
    private FournisseurService fournisseurService;

    @GetMapping
    public ResponseEntity<Page<FournisseurDTO>> getAllFournisseurs(
            @RequestParam(required = false) Fournisseur.TypeFournisseur type,
            @RequestParam(required = false) Boolean actif,
            @RequestParam(required = false) String nom,
            Pageable pageable) {
        
        Page<FournisseurDTO> fournisseurs = fournisseurService.getAllFournisseurs(type, actif, nom, pageable);
        return ResponseEntity.ok(fournisseurs);
    }

    @GetMapping("/active")
    public ResponseEntity<List<FournisseurDTO>> getActiveFournisseurs(
            @RequestParam(required = false) Fournisseur.TypeFournisseur type) {
        
        List<FournisseurDTO> fournisseurs = fournisseurService.getActiveFournisseurs(type);
        return ResponseEntity.ok(fournisseurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FournisseurDTO> getFournisseurById(@PathVariable Long id) {
        FournisseurDTO fournisseur = fournisseurService.getFournisseurById(id);
        return ResponseEntity.ok(fournisseur);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FournisseurDTO> createFournisseur(@Valid @RequestBody FournisseurDTO fournisseurDTO) {
        FournisseurDTO createdFournisseur = fournisseurService.createFournisseur(fournisseurDTO);
        return ResponseEntity.ok(createdFournisseur);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FournisseurDTO> updateFournisseur(
            @PathVariable Long id,
            @Valid @RequestBody FournisseurDTO fournisseurDTO) {
        
        FournisseurDTO updatedFournisseur = fournisseurService.updateFournisseur(id, fournisseurDTO);
        return ResponseEntity.ok(updatedFournisseur);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteFournisseur(@PathVariable Long id) {
        fournisseurService.deleteFournisseur(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FournisseurDTO> activateFournisseur(@PathVariable Long id) {
        FournisseurDTO fournisseur = fournisseurService.activateFournisseur(id);
        return ResponseEntity.ok(fournisseur);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FournisseurDTO> deactivateFournisseur(@PathVariable Long id) {
        FournisseurDTO fournisseur = fournisseurService.deactivateFournisseur(id);
        return ResponseEntity.ok(fournisseur);
    }
}