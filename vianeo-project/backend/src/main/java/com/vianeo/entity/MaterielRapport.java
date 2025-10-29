package com.vianeo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "materiel_rapport")
public class MaterielRapport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rapport_id", nullable = false)
    private RapportChantier rapport;
    
    @NotBlank
    private String designation;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private TypeMateriel type;
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal quantite = BigDecimal.ZERO;
    
    private String unite = "unité";
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal prixUnitaire = BigDecimal.ZERO;
    
    private String fournisseur;
    
    private boolean avecChauffeur = false; // Pour location matériel
    
    // Constructors
    public MaterielRapport() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public RapportChantier getRapport() { return rapport; }
    public void setRapport(RapportChantier rapport) { this.rapport = rapport; }
    
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    
    public TypeMateriel getType() { return type; }
    public void setType(TypeMateriel type) { this.type = type; }
    
    public BigDecimal getQuantite() { return quantite; }
    public void setQuantite(BigDecimal quantite) { this.quantite = quantite; }
    
    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }
    
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    
    public String getFournisseur() { return fournisseur; }
    public void setFournisseur(String fournisseur) { this.fournisseur = fournisseur; }
    
    public boolean isAvecChauffeur() { return avecChauffeur; }
    public void setAvecChauffeur(boolean avecChauffeur) { this.avecChauffeur = avecChauffeur; }
    
    public enum TypeMateriel {
        MATERIEL_INTERNE, LOCATION_MATERIEL, TRANSPORT, PRESTATION_EXTERNE, MATERIAUX
    }
}