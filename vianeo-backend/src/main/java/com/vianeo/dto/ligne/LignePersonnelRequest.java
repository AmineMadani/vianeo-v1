package com.vianeo.dto.ligne;

import com.vianeo.model.enums.CategoriePersonnel;
import com.vianeo.model.enums.TypeTravail;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class LignePersonnelRequest {
    
    @NotNull(message = "La catégorie est obligatoire")
    private CategoriePersonnel categorie;
    
    @NotNull(message = "L'utilisateur est obligatoire")
    private Long utilisateurId;
    
    @NotNull(message = "Le type de travail est obligatoire")
    private TypeTravail typeTravail;
    
    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private BigDecimal quantite;
    
    @NotNull(message = "Le prix unitaire est obligatoire")
    @Positive(message = "Le prix unitaire doit être positif")
    private BigDecimal pu;
    
    // Constructeurs
    public LignePersonnelRequest() {}
    
    public LignePersonnelRequest(CategoriePersonnel categorie, Long utilisateurId, 
                                TypeTravail typeTravail, BigDecimal quantite, BigDecimal pu) {
        this.categorie = categorie;
        this.utilisateurId = utilisateurId;
        this.typeTravail = typeTravail;
        this.quantite = quantite;
        this.pu = pu;
    }
    
    // Getters et Setters
    public CategoriePersonnel getCategorie() {
        return categorie;
    }
    
    public void setCategorie(CategoriePersonnel categorie) {
        this.categorie = categorie;
    }
    
    public Long getUtilisateurId() {
        return utilisateurId;
    }
    
    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
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