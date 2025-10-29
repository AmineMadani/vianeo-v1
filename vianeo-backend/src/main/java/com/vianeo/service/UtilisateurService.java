// com.vianeo.service.UtilisateurService
package com.vianeo.service;

import com.vianeo.model.entity.Entite;
import com.vianeo.model.entity.Utilisateur;
import com.vianeo.repository.EntiteRepository;
import com.vianeo.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // <--- ajoute cet import

import java.util.List;

@Service
public class UtilisateurService {

    private final UtilisateurRepository repo;
    private final EntiteRepository entiteRepo;

    public UtilisateurService(UtilisateurRepository repo, EntiteRepository entiteRepo) {
        this.repo = repo;
        this.entiteRepo = entiteRepo;
    }

    /* ===== READ ===== */



    @Transactional(readOnly = true)
    public List<Utilisateur> findByActif(boolean actif) {
        return repo.findAll().stream()
                .filter(u -> Boolean.TRUE.equals(u.getActif()) == actif)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> findByProfil(String profilUpper) {
        return repo.findByProfilAndActifTrue(profilUpper);
    }

    @Transactional(readOnly = true)
    public Utilisateur findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable: id=" + id));
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> findAll() {
        // AVANT: return repo.findAllActiveOrderByNomPrenom();
        return repo.findAllByOrderByNomAscPrenomAsc(); // <-- ramène actifs + inactifs
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> findByActif(Boolean actif) {
        if (actif == null) {
            return findAll(); // pas de filtre => tout
        }
        return repo.findAll().stream()
                .filter(u -> Boolean.TRUE.equals(u.getActif()) == actif)
                .sorted((a,b) -> {
                    int c = a.getNom().compareToIgnoreCase(b.getNom());
                    return c != 0 ? c : a.getPrenom().compareToIgnoreCase(b.getPrenom());
                })
                .toList();
    }

    /* ===== WRITE ===== */

    @Transactional
    public Utilisateur create(Utilisateur u, Long entiteId) {
        u.setId(null);
        if (u.getActif() == null) u.setActif(true);

        // Normaliser profil (frontend peut envoyer 'encadrant'/'operationnel')
        if (StringUtils.hasText(u.getProfil())) {
            u.setProfil(u.getProfil().trim().toUpperCase());
        } else {
            u.setProfil("OPERATIONNEL");
        }

        // Éviter la violation @NotBlank: valoriser metier si vide
        if (!StringUtils.hasText(u.getMetier())) {
            // défaut cohérent avec le profil
            u.setMetier("ENCADRANT".equals(u.getProfil()) ? "Encadrant" : "Opérationnel");
        } else {
            u.setMetier(u.getMetier().trim());
        }

        // Entité obligatoire
        if (entiteId == null) {
            throw new IllegalArgumentException("entiteId est obligatoire");
        }
        Entite entite = entiteRepo.findById(entiteId)
                .orElseThrow(() -> new IllegalArgumentException("Entite introuvable: id=" + entiteId));
        u.setEntite(entite);

        return repo.save(u);
    }

    @Transactional
    public Utilisateur update(Long id, Utilisateur patch, Long entiteId) {
        Utilisateur cur = findById(id);

        if (patch.getNom() != null) cur.setNom(patch.getNom());
        if (patch.getPrenom() != null) cur.setPrenom(patch.getPrenom());

        if (patch.getProfil() != null) {
            cur.setProfil(patch.getProfil().trim().toUpperCase());
        }

        if (patch.getMetier() != null) {
            String m = patch.getMetier().trim();
            // si on veut éviter une nouvelle violation @NotBlank en update
            cur.setMetier(StringUtils.hasText(m)
                    ? m
                    : ("ENCADRANT".equals(cur.getProfil()) ? "Encadrant" : "Opérationnel"));
        }

        if (patch.getTaux() != null) cur.setTaux(patch.getTaux());
        if (patch.getActif() != null) cur.setActif(patch.getActif());

        if (entiteId != null) {
            Entite entite = entiteRepo.findById(entiteId)
                    .orElseThrow(() -> new IllegalArgumentException("Entite introuvable: id=" + entiteId));
            cur.setEntite(entite);
        }

        return repo.save(cur);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
