package com.vianeo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})

@Entity
@Table(name = "affectation_personnel", schema = "vianeo",
       indexes = @Index(name = "idx_affectation_chantier_user", columnList = "chantier_id, personnel_id"))
public class AffectationPersonnel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chantier_id", nullable = false)
    private Chantier chantier;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_id", nullable = false)
    private Utilisateur personnel;
    
    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;
    
    @Column(name = "date_fin")
    private LocalDate dateFin;
    
    // Constructeurs
    public AffectationPersonnel() {}
    
    public AffectationPersonnel(Chantier chantier, Utilisateur utilisateur, LocalDate dateDebut) {
        this.chantier = chantier;
        this.personnel = utilisateur;
        this.dateDebut = dateDebut;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Chantier getChantier() {
        return chantier;
    }
    
    public void setChantier(Chantier chantier) {
        this.chantier = chantier;
    }
    
    public Utilisateur getPersonnel() {
        return personnel;
    }
    
    public void setPersonnel(Utilisateur personnel) {
        this.personnel = personnel;
    }
    
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public LocalDate getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
}