package com.vianeo.dto;

import com.vianeo.entity.Chantier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ChantierDTO {
    
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String nom;
    
    @Size(max = 200)
    private String adresse;
    
    @Size(max = 50)
    private String ville;
    
    @Size(max = 10)
    private String codePostal;
    
    @Size(max = 100)
    private String client;
    
    private LocalDate dateDebut;
    
    private LocalDate dateFin;
    
    private Chantier.Statut statut;
    
    private String description;
    
    private List<UserDTO> chefs;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Constructors
    public ChantierDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    
    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }
    
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public Chantier.Statut getStatut() { return statut; }
    public void setStatut(Chantier.Statut statut) { this.statut = statut; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<UserDTO> getChefs() { return chefs; }
    public void setChefs(List<UserDTO> chefs) { this.chefs = chefs; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}