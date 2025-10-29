package com.vianeo.dto;

import com.vianeo.entity.MaterielRapport;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class MaterielRapportDTO {
    
    private Long id;
    
    @NotBlank
    private String designation;
    
    private MaterielRapport.TypeMateriel type;
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal quantite = BigDecimal.ZERO;
    
    private String unite = "unité";
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal prixUnitaire = BigDecimal.ZERO;
    
    private String fournisseur;
    
    private boolean avecChauffeur = false;
    
    // Constructors
    public MaterielRapportDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    
    public MaterielRapport.TypeMateriel getType() { return type; }
    public void setType(MaterielRapport.TypeMateriel type) { this.type = type; }
    
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
}