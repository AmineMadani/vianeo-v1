package com.vianeo.dto;

import com.vianeo.entity.Commande;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CommandeDTO {
    
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    private String numeroCommande;
    
    @NotNull
    private Long chantierId;
    
    private String chantierNom;
    
    private LocalDate dateCommande;
    
    private LocalDate dateLivraison;
    
    @Size(max = 200)
    private String description;
    
    private Commande.StatutCommande statut;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Constructors
    public CommandeDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumeroCommande() { return numeroCommande; }
    public void setNumeroCommande(String numeroCommande) { this.numeroCommande = numeroCommande; }
    
    public Long getChantierId() { return chantierId; }
    public void setChantierId(Long chantierId) { this.chantierId = chantierId; }
    
    public String getChantierNom() { return chantierNom; }
    public void setChantierNom(String chantierNom) { this.chantierNom = chantierNom; }
    
    public LocalDate getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDate dateCommande) { this.dateCommande = dateCommande; }
    
    public LocalDate getDateLivraison() { return dateLivraison; }
    public void setDateLivraison(LocalDate dateLivraison) { this.dateLivraison = dateLivraison; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Commande.StatutCommande getStatut() { return statut; }
    public void setStatut(Commande.StatutCommande statut) { this.statut = statut; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}