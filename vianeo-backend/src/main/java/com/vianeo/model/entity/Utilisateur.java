package com.vianeo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})

@Entity
@Table(name = "personnel", schema = "vianeo")
public class Utilisateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", nullable = false, length = 80)
    @NotBlank
    @Size(max = 80)
    private String nom;
    
    @Column(name = "prenom", nullable = false, length = 80)
    @NotBlank
    @Size(max = 80)
    private String prenom;
    
    @Column(name = "metier", nullable = false, length = 64)
    @NotBlank
    @Size(max = 64)
    private String metier;
    
    @Column(name = "profil", nullable = false, length = 32)
    @NotBlank
    @Size(max = 32)
    private String profil;
    
    @Column(name = "actif", nullable = false)
    private Boolean actif = true;
    
    @Column(name = "taux", precision = 12, scale = 2)
    private BigDecimal taux;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entite_id", nullable = false)
    private Entite entite;

    // Constructors
    public Utilisateur() {}

    public Utilisateur(String nom, String prenom, String metier, String profil, Entite entite) {
        this.nom = nom;
        this.prenom = prenom;
        this.metier = metier;
        this.profil = profil;
        this.entite = entite;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMetier() {
        return metier;
    }

    public void setMetier(String metier) {
        this.metier = metier;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public BigDecimal getTaux() {
        return taux;
    }

    public void setTaux(BigDecimal taux) {
        this.taux = taux;
    }

    public Entite getEntite() {
        return entite;
    }

    public void setEntite(Entite entite) {
        this.entite = entite;
    }
}