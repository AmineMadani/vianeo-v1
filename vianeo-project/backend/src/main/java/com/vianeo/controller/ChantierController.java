package com.vianeo.controller;

import com.vianeo.dto.ChantierDTO;
import com.vianeo.entity.Chantier;
import com.vianeo.security.UserPrincipal;
import com.vianeo.service.ChantierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chantiers")
public class ChantierController {

    @Autowired
    private ChantierService chantierService;

    @GetMapping
    public ResponseEntity<Page<ChantierDTO>> getAllChantiers(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) Chantier.Statut statut,
            @RequestParam(required = false) String nom,
            Pageable pageable) {
        
        Page<ChantierDTO> chantiers;
        
        if (userPrincipal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            // Admin peut voir tous les chantiers
            chantiers = chantierService.getAllChantiers(statut, nom, pageable);
        } else {
            // Chef de chantier ne voit que ses chantiers
            chantiers = chantierService.getChantiersByChef(userPrincipal.getId(), statut, nom, pageable);
        }
        
        return ResponseEntity.ok(chantiers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChantierDTO> getChantierById(@PathVariable Long id) {
        ChantierDTO chantier = chantierService.getChantierById(id);
        return ResponseEntity.ok(chantier);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChantierDTO> createChantier(@Valid @RequestBody ChantierDTO chantierDTO) {
        ChantierDTO createdChantier = chantierService.createChantier(chantierDTO);
        return ResponseEntity.ok(createdChantier);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChantierDTO> updateChantier(
            @PathVariable Long id,
            @Valid @RequestBody ChantierDTO chantierDTO) {
        
        ChantierDTO updatedChantier = chantierService.updateChantier(id, chantierDTO);
        return ResponseEntity.ok(updatedChantier);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteChantier(@PathVariable Long id) {
        chantierService.deleteChantier(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/chefs/{chefId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addChefToChantier(@PathVariable Long id, @PathVariable Long chefId) {
        chantierService.addChefToChantier(id, chefId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/chefs/{chefId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeChefFromChantier(@PathVariable Long id, @PathVariable Long chefId) {
        chantierService.removeChefFromChantier(id, chefId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-chantiers")
    public ResponseEntity<List<ChantierDTO>> getMyChantiers(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<ChantierDTO> chantiers = chantierService.getChantiersByChefId(userPrincipal.getId());
        return ResponseEntity.ok(chantiers);
    }
}