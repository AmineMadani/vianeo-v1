package com.vianeo.dto.ligne;

import com.vianeo.model.enums.TypeTravail;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class LigneInterimRequest {
    
    @NotNull(message = "Le fournisseur est obligatoire")
    private Long fournisseurId;
    
    @Size(max = 80, message = "Le nom ne peut pas dépasser 80 caractères")
    private String nom;
    
    @Size(max = 80, message = "Le prénom ne peut pas dépasser 80 caractères")
    private String prenom;
    
    @NotNull(message = "Le type de travail est obligatoire")
    private TypeTravail typeTravail;
    
    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private BigDecimal quantite;
    
    @NotNull(message = "Le prix unitaire est obligatoire")
    @Positive(message = "Le prix unitaire doit être positif")
    private BigDecimal pu;
    
    // Constructeurs
    public LigneInterimRequest() {}
    
    public LigneInterimRequest(Long fournisseurId, String nom, String prenom, 
                              TypeTravail typeTravail, BigDecimal quantite, BigDecimal pu) {
        this.fournisseurId = fournisseurId;
        this.nom = nom;
        this.prenom = prenom;
        this.typeTravail = typeTravail;
        this.quantite = quantite;
        this.pu = pu;
    }
    
    // Getters et Setters
    public Long getFournisseurId() {
        return fournisseurId;
    }
    
    public void setFournisseurId(Long fournisseurId) {
        this.fournisseurId = fournisseurId;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public TypeTravail getTypeTravail() {
        return typeTravail;
    }
    
    public void setTypeTravail(TypeTravail typeTravail) {
        this.typeTravail = typeTravail;
    }
    
    public BigDecimal getQuantite() {
        return quantite;
    }
    
    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }
    
    public BigDecimal getPu() {
        return pu;
    }
    
    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }
}