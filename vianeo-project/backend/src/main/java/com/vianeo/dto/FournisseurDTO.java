package com.vianeo.dto;

import com.vianeo.entity.Fournisseur;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class FournisseurDTO {
    
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String nom;
    
    private Fournisseur.TypeFournisseur type;
    
    @Size(max = 200)
    private String adresse;
    
    @Size(max = 50)
    private String ville;
    
    @Size(max = 10)
    private String codePostal;
    
    @Size(max = 20)
    private String telephone;
    
    @Size(max = 100)
    private String email;
    
    @Size(max = 100)
    private String contactPrincipal;
    
    private boolean actif;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Constructors
    public FournisseurDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public Fournisseur.TypeFournisseur getType() { return type; }
    public void setType(Fournisseur.TypeFournisseur type) { this.type = type; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getContactPrincipal() { return contactPrincipal; }
    public void setContactPrincipal(String contactPrincipal) { this.contactPrincipal = contactPrincipal; }
    
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}