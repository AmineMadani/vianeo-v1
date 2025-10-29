package com.vianeo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})

@Entity
@Table(name = "chantier", schema = "vianeo")
public class Chantier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code", unique = true, nullable = false, length = 80)
    @NotBlank
    @Size(max = 80)
    private String code;
    
    @Column(name = "libelle", nullable = false, length = 160)
    @NotBlank
    @Size(max = 160)
    private String libelle;
    
    @Column(name = "adresse", columnDefinition = "TEXT")
    private String adresse;
    
    @Column(name = "ville", length = 80)
    @Size(max = 80)
    private String ville;
    
    @Column(name = "code_postal", length = 12)
    @Size(max = 12)
    private String codePostal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Utilisateur responsable;
    
    @Column(name = "actif", nullable = false)
    private Boolean actif = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entite_id", nullable = false)
    private Entite entite;

    // Constructors
    public Chantier() {}

    public Chantier(String code, String libelle, Entite entite) {
        this.code = code;
        this.libelle = libelle;
        this.entite = entite;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public Utilisateur getResponsable() {
        return responsable;
    }

    public void setResponsable(Utilisateur responsable) {
        this.responsable = responsable;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Entite getEntite() {
        return entite;
    }

    public void setEntite(Entite entite) {
        this.entite = entite;
    }
}