// com.vianeo.service.FournisseurService
package com.vianeo.service;

import com.vianeo.model.entity.Fournisseur;
import com.vianeo.model.enums.TypeFournisseur;
import com.vianeo.repository.FournisseurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FournisseurService {

    private final FournisseurRepository repo;

    public FournisseurService(FournisseurRepository repo) {
        this.repo = repo;
    }

    /* ========== READ ========== */

    @Transactional(readOnly = true)
    public List<Fournisseur> findAllOrderByCode() {
        return repo.findAllByOrderByCode();
    }

    @Transactional(readOnly = true)
    public List<Fournisseur> findAllActifs() {
        return repo.findByActifTrueOrderByCode();
    }

    @Transactional(readOnly = true)
    public List<Fournisseur> findByType(TypeFournisseur type) {
        return repo.findByTypeAndActifTrueOrderByCode(type);
    }

    @Transactional(readOnly = true)
    public List<Fournisseur> findActiveByType(TypeFournisseur type) {
        return findByType(type);
    }

    @Transactional(readOnly = true)
    public List<Fournisseur> findByActif(Boolean actif) {
        return repo.findByActifOrderByCode(actif);
    }

    /**
     * Filtrage combiné. Si aucun filtre n'est donné → renvoie tous (actifs + inactifs).
     * Si 'search' est présent, on filtre en mémoire sur le code.
     */
    @Transactional(readOnly = true)
    public List<Fournisseur> findByFilters(TypeFournisseur type, String search, Boolean actif) {
        List<Fournisseur> base;

        if (type != null && actif != null) {
            base = repo.findByTypeAndActifOrderByCode(type, actif);
        } else if (type != null) {
            // renvoyer tous pour ce type si 'actif' n'est pas précisé
            base = repo.findByTypeAndActifOrderByCode(type, true);
            base.addAll(repo.findByTypeAndActifOrderByCode(type, false));
        } else if (actif != null) {
            base = repo.findByActifOrderByCode(actif);
        } else {
            base = repo.findAllByOrderByCode();
        }

        if (search != null && !search.isBlank()) {
            final String needle = search.toLowerCase();
            return base.stream()
                    .filter(f -> f.getCode() != null && f.getCode().toLowerCase().contains(needle))
                    .toList();
        }
        return base;
    }

    @Transactional(readOnly = true)
    public Fournisseur findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fournisseur introuvable: id=" + id));
    }

    /* ========== WRITE ========== */

    @Transactional
    public Fournisseur create(Fournisseur f) {
        f.setId(null); // force génération
        if (f.getActif() == null) f.setActif(true);

        // (optionnel) éviter les doublons code
        repo.findByCode(f.getCode()).ifPresent(x -> {
            throw new IllegalArgumentException("Le code fournisseur existe déjà: " + f.getCode());
        });

        return repo.save(f);
    }

    @Transactional
    public Fournisseur update(Long id, Fournisseur patch) {
        Fournisseur cur = findById(id);
        if (patch.getCode() != null) cur.setCode(patch.getCode());
        if (patch.getType() != null) cur.setType(patch.getType());
        if (patch.getActif() != null) cur.setActif(patch.getActif());
        return repo.save(cur);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
