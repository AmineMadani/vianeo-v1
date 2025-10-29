package com.vianeo.dto.ligne;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class LigneMatInterneRequest {
    
    @NotNull(message = "L'article est obligatoire")
    private Long articleId;
    
    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private BigDecimal quantite;
    
    @NotNull(message = "Le prix unitaire est obligatoire")
    @Positive(message = "Le prix unitaire doit être positif")
    private BigDecimal pu;
    
    // Constructeurs
    public LigneMatInterneRequest() {}
    
    public LigneMatInterneRequest(Long articleId, BigDecimal quantite, BigDecimal pu) {
        this.articleId = articleId;
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