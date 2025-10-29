package com.vianeo.repository;

import com.vianeo.entity.Chantier;
import com.vianeo.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
//public interface CommandeRepository extends JpaRepository<Commande, Long> {
public interface CommandeRepository extends JpaRepository<Commande, Long>, JpaSpecificationExecutor<Commande> {
    List<Commande> findByChantier(Chantier chantier);
    
    List<Commande> findByStatut(Commande.StatutCommande statut);
    
    @Query("SELECT c FROM Commande c WHERE c.chantier = :chantier ORDER BY c.dateCommande DESC")
    List<Commande> findByChantierOrderByDateCommandeDesc(@Param("chantier") Chantier chantier);
    
    @Query("SELECT c FROM Commande c WHERE c.dateCommande BETWEEN :dateDebut AND :dateFin")
    List<Commande> findByDateCommandeBetween(
        @Param("dateDebut") LocalDate dateDebut, 
        @Param("dateFin") LocalDate dateFin);
    
    @Query("SELECT COUNT(c) FROM Commande c WHERE c.chantier = :chantier AND c.statut = :statut")
    long countByChantierAndStatut(@Param("chantier") Chantier chantier, @Param("statut") Commande.StatutCommande statut);
}