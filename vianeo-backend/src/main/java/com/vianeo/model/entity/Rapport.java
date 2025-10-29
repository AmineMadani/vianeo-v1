package com.vianeo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vianeo.model.enums.StatutRapport;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})

@Entity
@Table(name = "rapport", schema = "vianeo",
       uniqueConstraints = @UniqueConstraint(columnNames = {"chantier_id", "jour"}))
public class Rapport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chantier_id", nullable = false)
    private Chantier chantier;
    
    @Column(name = "jour", nullable = false)
    private LocalDate jour;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    private Utilisateur auteur;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutRapport statut = StatutRapport.DRAFT;
    
    @Column(name = "commentaire_cdt", columnDefinition = "TEXT")
    private String commentaireCdt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructors
    public Rapport() {}

    public Rapport(Chantier chantier, LocalDate jour, Utilisateur auteur) {
        this.chantier = chantier;
        this.jour = jour;
        this.auteur = auteur;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
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

    public LocalDate getJour() {
        return jour;
    }

    public void setJour(LocalDate jour) {
        this.jour = jour;
    }

    public Utilisateur getAuteur() {
        return auteur;
    }

    public void setAuteur(Utilisateur auteur) {
        this.auteur = auteur;
    }

    public StatutRapport getStatut() {
        return statut;
    }

    public void setStatut(StatutRapport statut) {
        this.statut = statut;
    }

    public String getCommentaireCdt() {
        return commentaireCdt;
    }

    public void setCommentaireCdt(String commentaireCdt) {
        this.commentaireCdt = commentaireCdt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}