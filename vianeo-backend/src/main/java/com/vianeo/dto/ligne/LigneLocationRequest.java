package com.vianeo.dto.ligne;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class LigneLocationRequest {
    
    @NotNull(message = "L'article est obligatoire")
    private Long articleId;
    
    @NotNull(message = "Le fournisseur est obligatoire")
    private Long fournisseurId;
    
    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private BigDecimal quantite;
    
    @NotNull(message = "Le prix unitaire est obligatoire")
    @Positive(message = "Le prix unitaire doit être positif")
    private BigDecimal pu;
    
    // Constructeurs
    public LigneLocationRequest() {}
    
    public LigneLocationRequest(Long articleId, Long fournisseurId, BigDecimal quantite, BigDecimal pu) {
        this.articleId = articleId;
        this.fournisseurId = fournisseurId;
        this.quantite = quantite;
        this.pu = pu;
    }
    
    // Getters et Setters
    public Long getArticleId() {
        return articleId;
    }
    
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
    
    public Long getFournisseurId() {
        return fournisseurId;
    }
    
    public void setFournisseurId(Long fournisseurId) {
        this.fournisseurId = fournisseurId;
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