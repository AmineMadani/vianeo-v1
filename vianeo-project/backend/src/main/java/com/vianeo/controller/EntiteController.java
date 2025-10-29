package com.vianeo.controller;

import com.vianeo.dto.EntiteDTO;
import com.vianeo.service.EntiteService;
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
@RequestMapping("/api/entites")
public class EntiteController {

    @Autowired
    private EntiteService entiteService;

    @GetMapping
    public ResponseEntity<List<EntiteDTO>> getAllEntites() {
        List<EntiteDTO> entites = entiteService.getAllEntites();
        return ResponseEntity.ok(entites);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntiteDTO> getEntiteById(@PathVariable Long id) {
        EntiteDTO entite = entiteService.getEntiteById(id);
        return ResponseEntity.ok(entite);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntiteDTO> createEntite(@Valid @RequestBody EntiteDTO entiteDTO) {
        EntiteDTO createdEntite = entiteService.createEntite(entiteDTO);
        return ResponseEntity.ok(createdEntite);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntiteDTO> updateEntite(
            @PathVariable Long id,
            @Valid @RequestBody EntiteDTO entiteDTO) {
        
        EntiteDTO updatedEntite = entiteService.updateEntite(id, entiteDTO);
        return ResponseEntity.ok(updatedEntite);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEntite(@PathVariable Long id) {
        entiteService.deleteEntite(id);
        return ResponseEntity.ok().build();
    }
}