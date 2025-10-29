// com.vianeo.controller.PersonnelController
package com.vianeo.controller;

import com.vianeo.dto.UtilisateurDto;
import com.vianeo.mapper.UtilisateurMapper;
import com.vianeo.model.entity.Utilisateur;
import com.vianeo.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/personnel")
@PreAuthorize("hasRole('ADMIN') or hasRole('CDT') or hasRole('CC') or hasRole('READONLY')")
public class PersonnelController {

    private final UtilisateurService service;

    public PersonnelController(UtilisateurService service) {
        this.service = service;
    }

    /** Liste avec filtres simples: type_personnel (profil), actif */
    @GetMapping
    public ResponseEntity<List<UtilisateurDto>> list(
            @RequestParam(required = false, name = "type") String typePersonnel,
            @RequestParam(required = false) Boolean actif
    ) {
        // 1) on part de TOUT (y compris inactifs)
        List<Utilisateur> data = service.findAll();

        // 2) filtre par type/profil si fourni
        if (typePersonnel != null && !typePersonnel.isBlank()) {
            String profil = UtilisateurMapper.normalizeProfil(typePersonnel); // ex. "encadrant" -> "ENCADRANT"
            data = data.stream()
                    .filter(u -> profil.equalsIgnoreCase(u.getProfil()))
                    .toList();
        }

        // 3) filtre par actif si fourni (true/false)
        if (actif != null) {
            data = data.stream()
                    .filter(u -> Boolean.TRUE.equals(u.getActif()) == actif)
                    .toList();
        }

        return ResponseEntity.ok(data.stream().map(UtilisateurMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(UtilisateurMapper.toDto(service.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CDT')")
    public ResponseEntity<UtilisateurDto> create(@RequestBody UtilisateurDto dto) {
        var entity = UtilisateurMapper.toEntity(dto);
        var saved = service.create(entity, dto.getEntiteId());
        return ResponseEntity
                .created(URI.create("/api/personnel/" + saved.getId()))
                .body(UtilisateurMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CDT')")
    public ResponseEntity<UtilisateurDto> update(@PathVariable Long id, @RequestBody UtilisateurDto dto) {
        var entity = UtilisateurMapper.toEntity(dto);
        var updated = service.update(id, entity, dto.getEntiteId());
        return ResponseEntity.ok(UtilisateurMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CDT')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
