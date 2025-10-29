package com.vianeo.controller;

import com.vianeo.dto.CreateRapportRequest;
import com.vianeo.dto.RapportLite;
import com.vianeo.dto.WeekRapportResponse;
import com.vianeo.dto.ligne.*;
import com.vianeo.dto.rapport.RefuserRapportRequest;
import com.vianeo.model.entity.*;
import com.vianeo.model.entity.Rapport;
import com.vianeo.model.enums.StatutRapport;
import com.vianeo.repository.*;
import com.vianeo.repository.RapportRepository;
import com.vianeo.service.LigneService;
import com.vianeo.service.RapportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rapports")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class RapportController {
    
    @Autowired
    private RapportService rapportService;
    
    @Autowired
    private LigneService ligneService;
    
    @Autowired
    private LignePersonnelRepository lignePersonnelRepository;
    
    @Autowired
    private LigneInterimRepository ligneInterimRepository;
    
    @Autowired
    private LigneMatInterneRepository ligneMatInterneRepository;
    
    @Autowired
    private LigneLocSsChRepository ligneLocSsChRepository;
    
    @Autowired
    private LigneLocAvecChRepository ligneLocAvecChRepository;
    
    @Autowired
    private LigneTransportRepository ligneTransportRepository;
    
    @Autowired
    private LignePrestaExtRepository lignePrestaExtRepository;
    
    @Autowired
    private LigneMateriauxRepository ligneMateriauxRepository;

    @Autowired
    private RapportTotauxRepository rapportTotauxRepository;


    /*@GetMapping("/{id}")
    public ResponseEntity<Rapport> getRapportById(@PathVariable Long id) {
        return rapportService.getRapportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    } */

    // === LIGNES ===
    
    // Ligne Personnel
    @PostMapping("/{id}/lignes/personnel")
    @PreAuthorize("hasAnyRole('ADMIN', 'CDT', 'CC')")
    public ResponseEntity<LignePersonnel> createLignePersonnel(
            @PathVariable Long id, 
            @Valid @RequestBody LignePersonnelRequest request) {
        try {
            LignePersonnel ligne = ligneService.createLignePersonnel(id, request);
            return ResponseEntity.ok(ligne);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/lignes/personnel")
    public ResponseEntity<List<LignePersonnel>> getLignesPersonnel(@PathVariable Long id) {
        List<LignePersonnel> lignes = lignePersonnelRepository.findByRapportIdOrderById(id);
        return ResponseEntity.ok(lignes);
    }
    
    // Ligne Intérim
    @PostMapping("/{id}/lignes/interim")
    @PreAuthorize("hasAnyRole('ADMIN', 'CDT', 'CC')")
    public ResponseEntity<LigneInterim> createLigneInterim(
            @PathVariable Long id, 
            @Valid @RequestBody LigneInterimRequest request) {
        try {
            LigneInterim ligne = ligneService.createLigneInterim(id, request);
            return ResponseEntity.ok(ligne);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/lignes/interim")
    public ResponseEntity<List<LigneInterim>> getLignesInterim(@PathVariable Long id) {
        List<LigneInterim> lignes = ligneInterimRepository.findByRapportIdOrderById(id);
        return ResponseEntity.ok(lignes);
    }
    
    // Ligne Matériel Interne
    @PostMapping("/{id}/lignes/matInterne")
    @PreAuthorize("hasAnyRole('ADMIN', 'CDT', 'CC')")
    public ResponseEntity<LigneMatInterne> createLigneMatInterne(
            @PathVariable Long id, 
            @Valid @RequestBody LigneMatInterneRequest request) {
        try {
            LigneMatInterne ligne = ligneService.createLigneMatInterne(id, request);
            return ResponseEntity.ok(ligne);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/lignes/matInterne")
    public ResponseEntity<List<LigneMatInterne>> getLignesMatInterne(@PathVariable Long id) {
        List<LigneMatInterne> lignes = ligneMatInterneRepository.findByRapportIdOrderById(id);
        return ResponseEntity.ok(lignes);
    }
    
    // Ligne Location Sans Chauffeur
    @PostMapping("/{id}/lignes/locSsCh")
    @PreAuthorize("hasAnyRole('ADMIN', 'CDT', 'CC')")
    public ResponseEntity<LigneLocSsCh> createLigneLocSsCh(
            @PathVariable Long id, 
            @Valid @RequestBody LigneLocationRequest request) {
        try {
            LigneLocSsCh ligne = ligneService.createLigneLocSsCh(id, request);
            return ResponseEntity.ok(ligne);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/lignes/locSsCh")
    public ResponseEntity<List<LigneLocSsCh>> getLignesLocSsCh(@PathVariable Long id) {
        List<LigneLocSsCh> lignes = ligneLocSsChRepository.findByRapportIdOrderById(id);
        return ResponseEntity.ok(lignes);
    }
    
    // Ligne Location Avec Chauffeur
    @PostMapping("/{id}/lignes/locAvecCh")
    @PreAuthorize("hasAnyRole('ADMIN', 'CDT', 'CC')")
    public ResponseEntity<LigneLocAvecCh> createLigneLocAvecCh(
            @PathVariable Long id, 
            @Valid @RequestBody LigneLocationRequest request) {
        try {
            LigneLocAvecCh ligne = ligneService.createLigneLocAvecCh(id, request);
            return ResponseEntity.ok(ligne);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/lignes/locAvecCh")
    public ResponseEntity<List<LigneLocAvecCh>> getLignesLocAvecCh(@PathVariable Long id) {
        List<LigneLocAvecCh> lignes = ligneLocAvecChRepository.findByRapportIdOrderById(id);
        return ResponseEntity.ok(lignes);
    }
    
    // Ligne Transport
    @PostMapping("/{id}/lignes/transport")
    @PreAuthorize("hasAnyRole('ADMIN', 'CDT', 'CC')")
    public ResponseEntity<LigneTransport> createLigneTransport(
            @PathVariable Long id, 
            @Valid @RequestBody LigneLocationRequest request) {
        try {
            LigneTransport ligne = ligneService.createLigneTransport(id, request);
            return ResponseEntity.ok(ligne);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/lignes/transport")
    public ResponseEntity<List<LigneTransport>> getLignesTransport(@PathVariable Long id) {
        List<LigneTransport> lignes = ligneTransportRepository.findByRapportIdOrderById(id);
        return ResponseEntity.ok(lignes);
    }
    
    // Ligne Prestation Externe
    @PostMapping("/{id}/lignes/prestaExt")
    @PreAuthorize("hasAnyRole('ADMIN', 'CDT', 'CC')")
    public ResponseEntity<LignePrestaExt> createLignePrestaExt(
            @PathVariable Long id, 
            @Valid @RequestBody LigneLocationRequest request) {
        try {
            LignePrestaExt ligne = ligneService.createLignePrestaExt(id, request);
            return ResponseEntity.ok(ligne);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/lignes/prestaExt")
    public ResponseEntity<List<LignePrestaExt>> getLignesPrestaExt(@PathVariable Long id) {
        List<LignePrestaExt> lignes = lignePrestaExtRepository.findByRapportIdOrderById(id);
        return ResponseEntity.ok(lignes);
    }
    
    // Ligne Matériaux
    @PostMapping("/{id}/lignes/materiaux")
    @PreAuthorize("hasAnyRole('ADMIN', 'CDT', 'CC')")
    public ResponseEntity<LigneMateriaux> createLigneMateriaux(
            @PathVariable Long id, 
            @Valid @RequestBody LigneMateriauxRequest request) {
        try {
            LigneMateriaux ligne = ligneService.createLigneMateriaux(id, request);
            return ResponseEntity.ok(ligne);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/lignes/materiaux")
    public ResponseEntity<List<LigneMateriaux>> getLignesMateriaux(@PathVariable Long id) {
        List<LigneMateriaux> lignes = ligneMateriauxRepository.findByRapportIdOrderById(id);
        return ResponseEntity.ok(lignes);
    }

    // com.vianeo.controller.RapportController
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CDT','CC')")
    public ResponseEntity<Rapport> createRapport(@RequestBody CreateRapportRequest req) {
        Rapport r = rapportService.createOrGetAndMaybePrefill(
                req.chantierId(),   // ✅ record accessor
                req.jour(),
                Boolean.TRUE.equals(req.prefill())
        );
        return ResponseEntity.ok(r);
    }


    // com.vianeo.controller.RapportController (ajout)
    @GetMapping("/{id:\\d+}/totaux")
    public ResponseEntity<?> getTotaux(@PathVariable Long id) {
        var total = rapportTotauxRepository.findTotalByRapportId(id).orElse(null);
        return ResponseEntity.ok(new Object() {
            public final Long rapportId = id;
            public final Object totalGeneral = total;
            public final Object totalPersonnel = null; // si tu as des vues dédiées, complète
            public final Object totalInterim   = null;
        });
    }

    @GetMapping("/search")
    public ResponseEntity<List<Rapport>> search(
            @RequestParam(required = false) Long chantierId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        if (chantierId != null && from != null && to != null) {
            return ResponseEntity.ok(rapportService.getRapportsByPeriod(chantierId, from, to));
        }
        if (chantierId != null) {
            return ResponseEntity.ok(rapportService.getRapportsByChantier(chantierId));
        }
        return ResponseEntity.ok(rapportService.getAllRapports());
    }


    // === WORKFLOW RAPPORT ===
    @PostMapping("/{id}/soumettre")
    @PreAuthorize("hasAnyRole('ADMIN', 'CDT', 'CC')")
    public ResponseEntity<Rapport> soumettre(@PathVariable Long id) {
        try {
            Rapport rapport = rapportService.soumettre(id);
            return ResponseEntity.ok(rapport);
        } catch (IllegalStateException e) {             // d'abord la plus spécifique
            return ResponseEntity.badRequest().build(); // p.ex. mauvais statut pour transition
        } catch (RuntimeException e) {                  // ensuite la plus générale
            return ResponseEntity.notFound().build();   // p.ex. rapport introuvable
        }
    }

    @PostMapping("/{id}/valider")
    @PreAuthorize("hasAnyRole('ADMIN', 'CDT')")
    public ResponseEntity<Rapport> valider(@PathVariable Long id) {
        try {
            Rapport rapport = rapportService.valider(id);
            return ResponseEntity.ok(rapport);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/refuser")
    @PreAuthorize("hasAnyRole('ADMIN', 'CDT')")
    public ResponseEntity<Rapport> refuser(@PathVariable Long id,
                                           @Valid @RequestBody RefuserRapportRequest request) {
        try {
            Rapport rapport = rapportService.refuser(id, request.getMotif());
            return ResponseEntity.ok(rapport);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === RAPPORTS SEMAINE ===
    @GetMapping("/week")
    public ResponseEntity<WeekRapportResponse> getRapportsWeek(
            @RequestParam Long chantierId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return ResponseEntity.ok(rapportService.getWeek(chantierId, weekStart));
    }

    // === RAPPORT PAR ID ===
// ⚠️ contrainte regex pour éviter que "week" soit pris pour un id
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Rapport> getRapportById(@PathVariable Long id) {
        return rapportService.getRapportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // com.vianeo.controller.RapportController (ajout)
    @GetMapping("/{id:\\d+}/check")
    public ResponseEntity<List<String>> checkObligatoires(@PathVariable Long id) {
        return ResponseEntity.ok(rapportService.checkObligatoires(id));
    }

    // ==================== OUVRIR RAPPORT DU JOUR ====================
    @PostMapping("/open-today")
    @PreAuthorize("hasAnyRole('ADMIN','CDT','CC')")
    public ResponseEntity<Rapport> openToday(
            @RequestParam Long chantierId,
            @RequestParam(required = false, defaultValue = "false") boolean prefillYesterday
    ) {
        Rapport r = rapportService.openToday(chantierId, prefillYesterday);
        return ResponseEntity.ok(r);
    }

    // ==================== LISTE CHEF : /api/rapports/my ====================
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('ADMIN','CDT','CC')")
    public ResponseEntity<Page<RapportLite>> listMyReports(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long chantierId,
            Pageable pageable
    ) {
        // récupère l’utilisateur courant (id) via SecurityContext
        Long userId = null;
        try {
            Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (p instanceof com.vianeo.security.CompteDetails cd && cd.getCompte() != null
                    && cd.getCompte().getUtilisateur() != null) {
                userId = cd.getCompte().getUtilisateur().getId();
            }
        } catch (Exception ignored) {}

        Page<RapportLite> page = rapportService.listMyReports(userId, chantierId, from, to, pageable);
        return ResponseEntity.ok(page);
    }

    // ==================== LISTE ADMIN : /api/rapports ====================
// (⚠️ tu as déjà un @GetMapping sans path → garde celui-ci et supprime l'ancien si doublon)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RapportLite>> listAllReports(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long chantierId,
            Pageable pageable
    ) {
        Page<RapportLite> page = rapportService.listAllReports(userId, chantierId, from, to, pageable);
        return ResponseEntity.ok(page);
    }

    // ==================== SUBMIT JOUR ====================
    @PostMapping("/{id}/submit-day")
    @PreAuthorize("hasAnyRole('ADMIN','CDT','CC')")
    public ResponseEntity<Rapport> submitDay(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(rapportService.submitDay(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== SUBMIT SEMAINE ====================
    @PostMapping("/week/{weekStart}/submit")
    @PreAuthorize("hasAnyRole('ADMIN','CDT','CC')")
    public ResponseEntity<Void> submitWeek(
            @RequestParam Long chantierId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        try {
            rapportService.submitWeek(chantierId, weekStart);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }



}