package com.vianeo.controller;

import com.vianeo.dto.RapportChantierDTO;
import com.vianeo.entity.RapportChantier;
import com.vianeo.security.UserPrincipal;
import com.vianeo.service.RapportChantierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rapports")
public class RapportChantierController {

    @Autowired
    private RapportChantierService rapportService;

    @GetMapping
    public ResponseEntity<Page<RapportChantierDTO>> getAllRapports(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(required = false) Long chantierId,
            @RequestParam(required = false) RapportChantier.Statut statut,
            Pageable pageable) {
        
        Page<RapportChantierDTO> rapports;
        
        if (userPrincipal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            // Admin peut voir tous les rapports
            rapports = rapportService.getAllRapports(dateDebut, dateFin, chantierId, statut, pageable);
        } else {
            // Chef de chantier ne voit que ses rapports
            rapports = rapportService.getRapportsByChef(userPrincipal.getId(), dateDebut, dateFin, chantierId, statut, pageable);
        }
        
        return ResponseEntity.ok(rapports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RapportChantierDTO> getRapportById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        RapportChantierDTO rapport = rapportService.getRapportById(id, userPrincipal.getId());
        return ResponseEntity.ok(rapport);
    }

    @PostMapping
    public ResponseEntity<RapportChantierDTO> createRapport(
            @Valid @RequestBody RapportChantierDTO rapportDTO,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        rapportDTO.setChefChantierId(userPrincipal.getId());
        RapportChantierDTO createdRapport = rapportService.createRapport(rapportDTO);
        return ResponseEntity.ok(createdRapport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RapportChantierDTO> updateRapport(
            @PathVariable Long id,
            @Valid @RequestBody RapportChantierDTO rapportDTO,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        RapportChantierDTO updatedRapport = rapportService.updateRapport(id, rapportDTO, userPrincipal.getId());
        return ResponseEntity.ok(updatedRapport);
    }

    @PutMapping("/{id}/submit")
    public ResponseEntity<RapportChantierDTO> submitRapport(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        RapportChantierDTO rapport = rapportService.submitRapport(id, userPrincipal.getId());
        return ResponseEntity.ok(rapport);
    }

    @PutMapping("/{id}/validate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RapportChantierDTO> validateRapport(@PathVariable Long id) {
        RapportChantierDTO rapport = rapportService.validateRapport(id);
        return ResponseEntity.ok(rapport);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRapport(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        rapportService.deleteRapport(id, userPrincipal.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/today")
    public ResponseEntity<RapportChantierDTO> getTodayRapport(
            @RequestParam Long chantierId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        RapportChantierDTO rapport = rapportService.getTodayRapport(chantierId, userPrincipal.getId());
        return ResponseEntity.ok(rapport);
    }

    @PostMapping("/duplicate/{id}")
    public ResponseEntity<RapportChantierDTO> duplicateRapport(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDate,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        RapportChantierDTO rapport = rapportService.duplicateRapport(id, newDate, userPrincipal.getId());
        return ResponseEntity.ok(rapport);
    }
}