package com.vianeo.controller;

import com.vianeo.dto.CommandeDTO;
import com.vianeo.entity.Commande;
import com.vianeo.service.CommandeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @GetMapping
    public ResponseEntity<Page<CommandeDTO>> getAllCommandes(
            @RequestParam(required = false) Long chantierId,
            @RequestParam(required = false) Commande.StatutCommande statut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            Pageable pageable) {
        
        Page<CommandeDTO> commandes = commandeService.getAllCommandes(chantierId, statut, dateDebut, dateFin, pageable);
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("/chantier/{chantierId}")
    public ResponseEntity<List<CommandeDTO>> getCommandesByChantier(@PathVariable Long chantierId) {
        List<CommandeDTO> commandes = commandeService.getCommandesByChantier(chantierId);
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDTO> getCommandeById(@PathVariable Long id) {
        CommandeDTO commande = commandeService.getCommandeById(id);
        return ResponseEntity.ok(commande);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommandeDTO> createCommande(@Valid @RequestBody CommandeDTO commandeDTO) {
        CommandeDTO createdCommande = commandeService.createCommande(commandeDTO);
        return ResponseEntity.ok(createdCommande);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommandeDTO> updateCommande(
            @PathVariable Long id,
            @Valid @RequestBody CommandeDTO commandeDTO) {
        
        CommandeDTO updatedCommande = commandeService.updateCommande(id, commandeDTO);
        return ResponseEntity.ok(updatedCommande);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommandeDTO> updateCommandeStatus(
            @PathVariable Long id,
            @RequestParam Commande.StatutCommande statut) {
        
        CommandeDTO commande = commandeService.updateCommandeStatus(id, statut);
        return ResponseEntity.ok(commande);
    }
}