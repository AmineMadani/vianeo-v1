package com.vianeo.controller;

import com.vianeo.model.entity.Chantier;
import com.vianeo.repository.ChantierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chantiers")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class ChantierController {
    
    @Autowired
    private ChantierRepository chantierRepository;

    @GetMapping
    public ResponseEntity<List<Chantier>> getAllChantiers() {
        List<Chantier> chantiers = chantierRepository.findAllActiveOrderByLibelle();
        return ResponseEntity.ok(chantiers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chantier> getChantierById(@PathVariable Long id) {
        return chantierRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Chantier> createChantier(@RequestBody Chantier chantier) {
        Chantier savedChantier = chantierRepository.save(chantier);
        return ResponseEntity.ok(savedChantier);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Chantier> updateChantier(@PathVariable Long id, @RequestBody Chantier chantier) {
        if (!chantierRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        chantier.setId(id);
        Chantier updatedChantier = chantierRepository.save(chantier);
        return ResponseEntity.ok(updatedChantier);
    }
}