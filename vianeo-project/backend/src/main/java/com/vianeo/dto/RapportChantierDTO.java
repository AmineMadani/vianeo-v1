package com.vianeo.dto;

import com.vianeo.entity.RapportChantier;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RapportChantierDTO {
    
    private Long id;
    
    @NotNull
    private LocalDate date;
    
    @NotNull
    private Long chantierId;
    
    private String chantierNom;
    
    @NotNull
    private Long chefChantierId;
    
    private String chefChantierNom;
    
    private RapportChantier.Statut statut;
    
    private String observations;
    
    private String problemes;
    
    private String securite;
    
    private String meteo;
    
    private List<PersonnelRapportDTO> personnel;
    
    private List<MaterielRapportDTO> materiel;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Constructors
    public RapportChantierDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public Long getChantierId() { return chantierId; }
    public void setChantierId(Long chantierId) { this.chantierId = chantierId; }
    
    public String getChantierNom() { return chantierNom; }
    public void setChantierNom(String chantierNom) { this.chantierNom = chantierNom; }
    
    public Long getChefChantierId() { return chefChantierId; }
    public void setChefChantierId(Long chefChantierId) { this.chefChantierId = chefChantierId; }
    
    public String getChefChantierNom() { return chefChantierNom; }
    public void setChefChantierNom(String chefChantierNom) { this.chefChantierNom = chefChantierNom; }
    
    public RapportChantier.Statut getStatut() { return statut; }
    public void setStatut(RapportChantier.Statut statut) { this.statut = statut; }
    
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    
    public String getProblemes() { return problemes; }
    public void setProblemes(String problemes) { this.problemes = problemes; }
    
    public String getSecurite() { return securite; }
    public void setSecurite(String securite) { this.securite = securite; }
    
    public String getMeteo() { return meteo; }
    public void setMeteo(String meteo) { this.meteo = meteo; }
    
    public List<PersonnelRapportDTO> getPersonnel() { return personnel; }
    public void setPersonnel(List<PersonnelRapportDTO> personnel) { this.personnel = personnel; }
    
    public List<MaterielRapportDTO> getMateriel() { return materiel; }
    public void setMateriel(List<MaterielRapportDTO> materiel) { this.materiel = materiel; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}