package com.vianeo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "personnel_rapport")
public class PersonnelRapport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rapport_id", nullable = false)
    private RapportChantier rapport;
    
    @NotBlank
    private String nom;
    
    @NotBlank
    private String prenom;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TypePersonnel type;
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal heuresLundi = BigDecimal.ZERO;
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal heuresMardi = BigDecimal.ZERO;
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal heuresMercredi = BigDecimal.ZERO;
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal heuresJeudi = BigDecimal.ZERO;
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal heuresVendredi = BigDecimal.ZERO;
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal heuresSamedi = BigDecimal.ZERO;
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal tauxJournalier = BigDecimal.ZERO;
    
    private String fournisseur; // Pour l'intérim
    
    // Constructors
    public PersonnelRapport() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public RapportChantier getRapport() { return rapport; }
    public void setRapport(RapportChantier rapport) { this.rapport = rapport; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public TypePersonnel getType() { return type; }
    public void setType(TypePersonnel type) { this.type = type; }
    
    public BigDecimal getHeuresLundi() { return heuresLundi; }
    public void setHeuresLundi(BigDecimal heuresLundi) { this.heuresLundi = heuresLundi; }
    
    public BigDecimal getHeuresMardi() { return heuresMardi; }
    public void setHeuresMardi(BigDecimal heuresMardi) { this.heuresMardi = heuresMardi; }
    
    public BigDecimal getHeuresMercredi() { return heuresMercredi; }
    public void setHeuresMercredi(BigDecimal heuresMercredi) { this.heuresMercredi = heuresMercredi; }
    
    public BigDecimal getHeuresJeudi() { return heuresJeudi; }
    public void setHeuresJeudi(BigDecimal heuresJeudi) { this.heuresJeudi = heuresJeudi; }
    
    public BigDecimal getHeuresVendredi() { return heuresVendredi; }
    public void setHeuresVendredi(BigDecimal heuresVendredi) { this.heuresVendredi = heuresVendredi; }
    
    public BigDecimal getHeuresSamedi() { return heuresSamedi; }
    public void setHeuresSamedi(BigDecimal heuresSamedi) { this.heuresSamedi = heuresSamedi; }
    
    public BigDecimal getTauxJournalier() { return tauxJournalier; }
    public void setTauxJournalier(BigDecimal tauxJournalier) { this.tauxJournalier = tauxJournalier; }
    
    public String getFournisseur() { return fournisseur; }
    public void setFournisseur(String fournisseur) { this.fournisseur = fournisseur; }
    
    public enum TypePersonnel {
        ENCADRANT, OPERATIONNEL, INTERIM
    }
}