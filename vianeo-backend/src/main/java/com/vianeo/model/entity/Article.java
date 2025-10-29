package com.vianeo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vianeo.model.enums.CategorieArticle;
import com.vianeo.model.enums.TypeArticle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})

@Entity
@Table(name = "article", schema = "vianeo")
public class Article {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 80)
    @Column(unique = true, nullable = false, length = 80)
    private String code;
    
    @NotBlank
    @Size(max = 160)
    @Column(nullable = false, length = 160)
    private String designation;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cat", nullable = false)
    private CategorieArticle categorie;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeArticle type;
    
    @Column(nullable = false)
    private Boolean actif = true;
    
    // Constructeurs
    public Article() {}
    
    public Article(String code, String designation, CategorieArticle categorie, TypeArticle type) {
        this.code = code;
        this.designation = designation;
        this.categorie = categorie;
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
    
    public String getDesignation() {
        return designation;
    }
    
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    
    public CategorieArticle getCategorie() {
        return categorie;
    }
    
    public void setCategorie(CategorieArticle categorie) {
        this.categorie = categorie;
    }
    
    public TypeArticle getType() {
        return type;
    }
    
    public void setType(TypeArticle type) {
        this.type = type;
    }
    
    public Boolean getActif() {
        return actif;
    }
    
    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}