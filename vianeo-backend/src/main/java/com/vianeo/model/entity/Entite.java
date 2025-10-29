package com.vianeo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})

@Entity
@Table(name = "entite", schema = "vianeo")
public class Entite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code", unique = true, nullable = false, length = 32)
    @NotBlank
    @Size(max = 32)
    private String code;
    
    @Column(name = "libelle", nullable = false, length = 128)
    @NotBlank
    @Size(max = 128)
    private String libelle;
    
    @Column(name = "actif", nullable = false)
    private Boolean actif = true;

    // Constructors
    public Entite() {}

    public Entite(String code, String libelle) {
        this.code = code;
        this.libelle = libelle;
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

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}