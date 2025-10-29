package com.vianeo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vianeo.model.enums.TypeTravail;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.math.BigDecimal;
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})

@Entity
@Table(name = "ligne_interim", schema = "vianeo",
       indexes = @Index(name = "idx_ligne_interim_rapport", columnList = "rapport_id"))
public class LigneInterim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rapport_id", nullable = false)
    private Rapport rapport;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;
    
    @Size(max = 80)
    @Column(length = 80)
    private String nom;
    
    @Size(max = 80)
    @Column(length = 80)
    private String prenom;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_travail", nullable = false)
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
    public LigneInterim() {}
    
    public LigneInterim(Rapport rapport, Fournisseur fournisseur, String nom, String prenom,
                       TypeTravail typeTravail, BigDecimal quantite, BigDecimal pu) {
        this.rapport = rapport;
        this.fournisseur = fournisseur;
        this.nom = nom;
        this.prenom = prenom;
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
    
    public Fournisseur getFournisseur() {
        return fournisseur;
    }
    
    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
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
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}