package com.vianeo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vianeo.model.enums.TypeFournisseur;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Type;
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})

@Entity
@Table(name = "fournisseur", schema = "vianeo")
public class Fournisseur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 64)
    @Column(unique = true, nullable = false, length = 64)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeFournisseur type;
    
    @Column(nullable = false)
    private Boolean actif = true;
    
    // Constructeurs
    public Fournisseur() {}
    
    public Fournisseur(String code, TypeFournisseur type) {
        this.code = code;
        this.type = type;
        this.actif = true;
    }
    
    // Getters et Setters
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
    
    public TypeFournisseur getType() {
        return type;
    }
    
    public void setType(TypeFournisseur type) {
        this.type = type;
    }
    
    public Boolean getActif() {
        return actif;
    }
    
    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}