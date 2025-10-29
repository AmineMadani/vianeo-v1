package com.vianeo.dto;

import com.vianeo.entity.PersonnelRapport;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PersonnelRapportDTO {
    
    private Long id;
    
    @NotBlank
    private String nom;
    
    @NotBlank
    private String prenom;
    
    private PersonnelRapport.TypePersonnel type;
    
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
    
    private String fournisseur;
    
    // Constructors
    public PersonnelRapportDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public PersonnelRapport.TypePersonnel getType() { return type; }
    public void setType(PersonnelRapport.TypePersonnel type) { this.type = type; }
    
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
}