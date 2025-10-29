package com.vianeo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vianeo.model.enums.CategoriePersonnel;
import com.vianeo.model.enums.TypeTravail;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.math.BigDecimal;
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})

@Entity
@Table(name = "ligne_personnel", schema = "vianeo",
       indexes = @Index(name = "idx_ligne_pers_rapport", columnList = "rapport_id"))
public class LignePersonnel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rapport_id", nullable = false)
    private Rapport rapport;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriePersonnel categorie;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_id", nullable = false)
    private Utilisateur utilisateur;
    
    @NotNull
    @Convert(converter = com.vianeo.model.converter.TypeTravailConverter.class)
    private TypeTravail typeTravail;
    
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
    public LignePersonnel() {}
    
    public LignePersonnel(Rapport rapport, CategoriePersonnel categorie, Utilisateur utilisateur, 
                         TypeTravail typeTravail, BigDecimal quantite, BigDecimal pu) {
        this.rapport = rapport;
        this.categorie = categorie;
        this.utilisateur = utilisateur;
        this.typeTravail = typeTravail;
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
    
    public CategoriePersonnel getCategorie() {
        return categorie;
    }
    
    public void setCategorie(CategoriePersonnel categorie) {
        this.categorie = categorie;
    }
    
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}