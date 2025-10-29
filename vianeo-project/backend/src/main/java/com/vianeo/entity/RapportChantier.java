package com.vianeo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rapports_chantier")
@EntityListeners(AuditingEntityListener.class)
public class RapportChantier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private LocalDate date;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chantier_id", nullable = false)
    private Chantier chantier;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chef_chantier_id", nullable = false)
    private User chefChantier;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut = Statut.BROUILLON;
    
    @Column(columnDefinition = "TEXT")
    private String observations;
    
    @Column(columnDefinition = "TEXT")
    private String problemes;
    
    @Column(columnDefinition = "TEXT")
    private String securite;
    
    private String meteo;
    
    @OneToMany(mappedBy = "rapport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PersonnelRapport> personnel = new HashSet<>();
    
    @OneToMany(mappedBy = "rapport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MaterielRapport> materiel = new HashSet<>();
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // Constructors
    public RapportChantier() {}
    
    public RapportChantier(LocalDate date, Chantier chantier, User chefChantier) {
        this.date = date;
        this.chantier = chantier;
        this.chefChantier = chefChantier;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public Chantier getChantier() { return chantier; }
    public void setChantier(Chantier chantier) { this.chantier = chantier; }
    
    public User getChefChantier() { return chefChantier; }
    public void setChefChantier(User chefChantier) { this.chefChantier = chefChantier; }
    
    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }
    
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    
    public String getProblemes() { return problemes; }
    public void setProblemes(String problemes) { this.problemes = problemes; }
    
    public String getSecurite() { return securite; }
    public void setSecurite(String securite) { this.securite = securite; }
    
    public String getMeteo() { return meteo; }
    public void setMeteo(String meteo) { this.meteo = meteo; }
    
    public Set<PersonnelRapport> getPersonnel() { return personnel; }
    public void setPersonnel(Set<PersonnelRapport> personnel) { this.personnel = personnel; }
    
    public Set<MaterielRapport> getMateriel() { return materiel; }
    public void setMateriel(Set<MaterielRapport> materiel) { this.materiel = materiel; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public enum Statut {
        BROUILLON, COMPLET, VALIDE
    }
}