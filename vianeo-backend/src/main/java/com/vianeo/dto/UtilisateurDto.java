package com.vianeo.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class UtilisateurDto {
    private Long id;
    private String nom;
    private String prenom;

    // front: "type_personnel"  → back: profil ("ENCADRANT"/"OPERATIONNEL")
    @JsonAlias({"type_personnel","profil"})
    private String profil;

    // front: "taux_journalier" → back: taux
    @JsonAlias({"taux_journalier","taux"})
    private BigDecimal taux;

    // front: "qualification" → back: metier
    @JsonAlias({"qualification","metier"})
    private String metier;

    private Boolean actif;

    // nécessaire car entite est NOT NULL
    private Long entiteId;

    public UtilisateurDto() {}
    public UtilisateurDto(Long id, String nom, String prenom, String profil,
                          BigDecimal taux, String metier, Boolean actif, Long entiteId) {
        this.id = id; this.nom = nom; this.prenom = prenom; this.profil = profil;
        this.taux = taux; this.metier = metier; this.actif = actif; this.entiteId = entiteId;
    }

    // getters/setters…
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getProfil() { return profil; }
    public void setProfil(String profil) { this.profil = profil; }
    public BigDecimal getTaux() { return taux; }
    public void setTaux(BigDecimal taux) { this.taux = taux; }
    public String getMetier() { return metier; }
    public void setMetier(String metier) { this.metier = metier; }
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    public Long getEntiteId() { return entiteId; }
    public void setEntiteId(Long entiteId) { this.entiteId = entiteId; }

    /* alias d'export pour que le front lise ses clés sans changer une ligne */
    @JsonProperty("type_personnel") public String getTypePersonnel() { return profil; }
    @JsonProperty("taux_journalier") public BigDecimal getTauxJournalier() { return taux; }
    @JsonProperty("qualification") public String getQualification() { return metier; }
}
