package com.vianeo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chantiers")
@EntityListeners(AuditingEntityListener.class)
public class Chantier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut = Statut.EN_PREPARATION;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "chantier_chefs",
        joinColumns = @JoinColumn(name = "chantier_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> chefs = new HashSet<>();
    
    @OneToMany(mappedBy = "chantier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<RapportChantier> rapports = new HashSet<>();
    
    @OneToMany(mappedBy = "chantier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Commande> commandes = new HashSet<>();
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // Constructors
    public Chantier() {}
    
    public Chantier(String nom, String adresse, String ville) {
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
    }
    
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
    
    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Set<User> getChefs() { return chefs; }
    public void setChefs(Set<User> chefs) { this.chefs = chefs; }
    
    public Set<RapportChantier> getRapports() { return rapports; }
    public void setRapports(Set<RapportChantier> rapports) { this.rapports = rapports; }
    
    public Set<Commande> getCommandes() { return commandes; }
    public void setCommandes(Set<Commande> commandes) { this.commandes = commandes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public enum Statut {
        EN_PREPARATION, EN_COURS, TERMINE, SUSPENDU
    }
}