package com.vianeo.mapper;

import com.vianeo.dto.UtilisateurDto;
import com.vianeo.model.entity.Entite;
import com.vianeo.model.entity.Utilisateur;

public class UtilisateurMapper {

    /** vers DTO */
    public static UtilisateurDto toDto(Utilisateur e) {
        if (e == null) return null;
        Long entiteId = (e.getEntite() != null ? e.getEntite().getId() : null);
        return new UtilisateurDto(
                e.getId(),
                e.getNom(),
                e.getPrenom(),
                e.getProfil(),    // "ENCADRANT"/"OPERATIONNEL" (ou autre string)
                e.getTaux(),
                e.getMetier(),
                e.getActif(),
                entiteId
        );
    }

    /** vers entity (sans l’entité liée) */
    public static Utilisateur toEntity(UtilisateurDto d) {
        if (d == null) return null;
        Utilisateur e = new Utilisateur();
        e.setId(d.getId());
        e.setNom(d.getNom());
        e.setPrenom(d.getPrenom());
        e.setProfil(normalizeProfil(d.getProfil()));      // upper
        e.setTaux(d.getTaux());
        e.setMetier(d.getMetier());
        e.setActif(Boolean.TRUE.equals(d.getActif()));
        // l’entité sera branchée au service
        return e;
    }

    public static String normalizeProfil(String p) {
        if (p == null) return "OPERATIONNEL";
        return p.trim().toUpperCase(); // encadrant|operationnel → ENCADRANT|OPERATIONNEL
    }

    /** utilitaire pour attacher l’entité */
    public static void attachEntite(Utilisateur u, Entite entite) {
        u.setEntite(entite);
    }
}
