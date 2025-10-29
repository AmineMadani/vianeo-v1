package com.vianeo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.math.BigDecimal;
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})

@Entity
@Table(name = "ligne_transport", schema = "vianeo")
public class LigneTransport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rapport_id", nullable = false)
    private Rapport rapport;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;
    
    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantite;
    
    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal pu;

    @Column(name = "total", precision = 14, scale = 2, insertable = false, updatable = false)
    @Generated(GenerationTime.ALWAYS)
    private BigDecimal total;
    
    // Constructeurs
    public LigneTransport() {}
    
    public LigneTransport(Rapport rapport, Article article, Fournisseur fournisseur, 
                         BigDecimal quantite, BigDecimal pu) {
        this.rapport = rapport;
        this.article = article;
        this.fournisseur = fournisseur;
        this.quantite = quantite;
        this.pu = pu;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Rapport getRapport() {
        return rapport;
    }
    
    public void setRapport(Rapport rapport) {
        this.rapport = rapport;
    }
    
    public Article getArticle() {
        return article;
    }
    
    public void setArticle(Article article) {
        this.article = article;
    }
    
    public Fournisseur getFournisseur() {
        return fournisseur;
    }
    
    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
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
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}