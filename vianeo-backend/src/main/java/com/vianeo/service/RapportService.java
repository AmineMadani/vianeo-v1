package com.vianeo.service;

import com.vianeo.dto.DailyRapportSummary;
import com.vianeo.dto.RapportLite;
import com.vianeo.dto.WeekRapportResponse;
import com.vianeo.model.entity.*;
import com.vianeo.model.enums.StatutRapport;
import com.vianeo.repository.*;
import com.vianeo.security.CompteDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RapportService {

    @Autowired
    private RapportRepository rapportRepository;

    @Autowired
    private LignePersonnelRepository lignePersonnelRepository;
    @Autowired
    private LigneInterimRepository ligneInterimRepository;

    public List<Rapport> getAllRapports() {
        return rapportRepository.findAll();
    }

    public Optional<Rapport> getRapportById(Long id) {
        return rapportRepository.findById(id);
    }

    public List<Rapport> getRapportsByChantier(Long chantierId) {
        return rapportRepository.findByChantierIdOrderByJourDesc(chantierId);
    }

    public List<Rapport> getRapportsByStatut(StatutRapport statut) {
        return rapportRepository.findByStatut(statut);
    }

    public List<Rapport> getRapportsByPeriod(Long chantierId, LocalDate dateDebut, LocalDate dateFin) {
        return rapportRepository.findByChantierAndPeriod(chantierId, dateDebut, dateFin);
    }

    public Rapport saveRapport(Rapport rapport) {
        return rapportRepository.save(rapport);
    }

    public Rapport soumettre(Long rapportId) {
        Rapport rapport = rapportRepository.findById(rapportId)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé"));

        if (rapport.getStatut() != StatutRapport.DRAFT) {
            throw new IllegalStateException("Seuls les rapports en brouillon peuvent être soumis");
        }

        rapport.setStatut(StatutRapport.EN_ATTENTE_VALIDATION);
        return rapportRepository.save(rapport);
    }

    public Rapport valider(Long rapportId) {
        Rapport rapport = rapportRepository.findById(rapportId)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé"));

        if (rapport.getStatut() != StatutRapport.EN_ATTENTE_VALIDATION) {
            throw new IllegalStateException("Seuls les rapports en attente peuvent être validés");
        }

        rapport.setStatut(StatutRapport.VALIDE);
        return rapportRepository.save(rapport);
    }

    public Rapport refuser(Long rapportId, String motif) {
        Rapport rapport = rapportRepository.findById(rapportId)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé"));

        if (rapport.getStatut() != StatutRapport.EN_ATTENTE_VALIDATION) {
            throw new IllegalStateException("Seuls les rapports en attente peuvent être refusés");
        }

        rapport.setStatut(StatutRapport.REFUSE);
        rapport.setCommentaireCdt(motif);
        return rapportRepository.save(rapport);
    }

    public void deleteRapport(Long id) {
        rapportRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return rapportRepository.existsById(id);
    }

    @Autowired
    private RapportTotauxRepository rapportTotauxRepository;
    @Autowired
    private ChantierRepository chantierRepository;

    public WeekRapportResponse getWeek(Long chantierId, LocalDate weekStart) {
        List<DailyRapportSummary> days = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate jour = weekStart.plusDays(i);

            Rapport rapport = rapportRepository.findByChantierIdAndJour(chantierId, jour)
                    .orElseGet(() -> {
                        Rapport r = new Rapport();
                        Chantier chantier = chantierRepository.findById(chantierId)
                                .orElseThrow(() -> new RuntimeException("Chantier non trouvé: " + chantierId));
                        r.setChantier(chantier);
                        r.setJour(jour);
                        r.setStatut(StatutRapport.DRAFT);

                        // Auteur = utilisateur connecté si dispo
                        Utilisateur auteur = resolveAuteur();
                        r.setAuteur(auteur);

                        return rapportRepository.save(r);
                    });

            BigDecimal total = rapportTotauxRepository.findTotalByRapportId(rapport.getId())
                    .orElse(BigDecimal.ZERO);

            days.add(new DailyRapportSummary(
                    jour,
                    rapport.getId(),
                    rapport.getStatut().name(),
                    total
            ));
        }

        return new WeekRapportResponse(chantierId, weekStart, days);
    }

    // com.vianeo.service.RapportService (ajout)
    public Rapport createOrGetAndMaybePrefill(Long chantierId, LocalDate jour, boolean prefill) {
        Rapport rapport = rapportRepository.findByChantierIdAndJour(chantierId, jour)
                .orElseGet(() -> {
                    Chantier ch = chantierRepository.findById(chantierId)
                            .orElseThrow(() -> new RuntimeException("Chantier non trouvé: " + chantierId));
                    Utilisateur auteur = resolveAuteur();
                    Rapport r = new Rapport(ch, jour, auteur);
                    r.setStatut(StatutRapport.DRAFT);
                    return rapportRepository.save(r);
                });

        if (prefill) {
            LocalDate veille = jour.minusDays(1);
            rapportRepository.findByChantierIdAndJour(chantierId, veille).ifPresent(prev -> {
                // clone “soft” des lignes (quantités = 0, PU identiques)
                lignePersonnelRepository.findByRapportIdOrderById(prev.getId()).forEach(lp -> {
                    var clone = new LignePersonnel(
                            rapport, lp.getCategorie(), lp.getUtilisateur(),
                            lp.getTypeTravail(), BigDecimal.ZERO, lp.getPu());
                    lignePersonnelRepository.save(clone);
                });
                ligneInterimRepository.findByRapportIdOrderById(prev.getId()).forEach(li -> {
                    var clone = new LigneInterim(
                            rapport, li.getFournisseur(), li.getNom(), li.getPrenom(),
                            li.getTypeTravail(), BigDecimal.ZERO, li.getPu());
                    ligneInterimRepository.save(clone);
                });
                // idem pour autres lignes si tu veux (matériel, transports…)
            });
        }
        return rapport;
    }

    // com.vianeo.service.RapportService (ajout)
    public List<String> checkObligatoires(Long rapportId) {
        Rapport r = rapportRepository.findById(rapportId)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé"));
        List<String> violations = new ArrayList<>();

        boolean hasEffectif = lignePersonnelRepository.existsByRapportId(r.getId())
                || ligneInterimRepository.existsByRapportId(r.getId());
        if (!hasEffectif) violations.add("Aucun effectif saisi (personnel ou intérim).");

        if (r.getCommentaireCdt() == null || r.getCommentaireCdt().isBlank()) {
            violations.add("Observations/Commentaires obligatoires.");
        }
        // TODO: matériel utilisé si tu veux le rendre obligatoire

        return violations;
    }

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private Utilisateur resolveAuteur() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CompteDetails compteDetails) {
                return compteDetails.getCompte().getUtilisateur();
                // ⚠️ adapte : si Compte → Utilisateur direct
                // ex: compteDetails.getCompte().getUtilisateur()
            }
        } catch (Exception ignored) {
        }

        // fallback : admin id=1
        return utilisateurRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Auteur par défaut (id=1) introuvable"));
    }

    private RapportLite toLite(Rapport r, BigDecimal total) {
        var ch = r.getChantier();
        var a = r.getAuteur();
        String auteurNom = (a == null) ? null :
                ((a.getNom() != null ? a.getNom() : "") + " " + (a.getPrenom() != null ? a.getPrenom() : "")).trim();
        return new RapportLite(
                r.getId(),
                r.getJour(),
                ch != null ? ch.getId() : null,
                ch != null ? ch.getCode() : null,
                ch != null ? ch.getLibelle() : null,
                a != null ? a.getId() : null,
                auteurNom,
                r.getStatut() != null ? r.getStatut().name() : null,
                total
        );
    }

    private Page<RapportLite> toPageLite(List<Rapport> list, Pageable pageable) {
        // tri du plus récent au plus ancien
        List<Rapport> sorted = list.stream()
                .sorted(Comparator.comparing(Rapport::getJour).reversed()
                        .thenComparing(Rapport::getId).reversed())
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), sorted.size());
        List<RapportLite> content = (start >= end) ? List.of() :
                sorted.subList(start, end).stream()
                        .map(r -> toLite(r, rapportTotauxRepository.findTotalByRapportId(r.getId()).orElse(null)))
                        .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, sorted.size());
    }

    // ==================== NOUVEAU : API “open today” ====================
    public Rapport openToday(Long chantierId, boolean prefillYesterday) {
        return createOrGetAndMaybePrefill(chantierId, LocalDate.now(), prefillYesterday);
    }

    // ==================== NOUVEAU : listes (chef/admin) ====================
    public Page<RapportLite> listMyReports(Long userId, Long chantierId, LocalDate from, LocalDate to, Pageable pageable) {
        // on part de tes méthodes existantes pour rester simple
        List<Rapport> base;
        if (chantierId != null && from != null && to != null) {
            base = getRapportsByPeriod(chantierId, from, to);
        } else if (chantierId != null) {
            base = getRapportsByChantier(chantierId);
        } else {
            base = getAllRapports();
        }
        // filtre user
        if (userId != null) {
            base = base.stream()
                    .filter(r -> r.getAuteur() != null && userId.equals(r.getAuteur().getId()))
                    .collect(Collectors.toList());
        }
        // filtre bornes si fournies seules
        if (from != null) base = base.stream().filter(r -> !r.getJour().isBefore(from)).collect(Collectors.toList());
        if (to != null) base = base.stream().filter(r -> !r.getJour().isAfter(to)).collect(Collectors.toList());

        return toPageLite(base, pageable);
    }

    public Page<RapportLite> listAllReports(Long userId, Long chantierId, LocalDate from, LocalDate to, Pageable pageable) {
        // pour admin : mêmes filtres, userId optionnel
        return listMyReports(userId, chantierId, from, to, pageable);
    }

    // ==================== NOUVEAU : workflow submit day / week ====================
    public Rapport submitDay(Long rapportId) {
        return soumettre(rapportId); // réutilise ta logique existante
    }

    public void submitWeek(Long chantierId, LocalDate weekStart) {
        // Vérifie que chaque jour de la semaine existe ET n’est pas DRAFT, sinon IllegalStateException
        for (int i = 0; i < 7; i++) {
            LocalDate jour = weekStart.plusDays(i);
            Rapport r = rapportRepository.findByChantierIdAndJour(chantierId, jour)
                    .orElseThrow(() -> new IllegalStateException("Rapport manquant pour le " + jour));
            if (r.getStatut() == StatutRapport.DRAFT) {
                throw new IllegalStateException("Jour " + jour + " encore en brouillon.");
            }
        }
    }

}