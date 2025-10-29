package com.vianeo.repository;

import com.vianeo.entity.Chantier;
import com.vianeo.entity.RapportChantier;
import com.vianeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RapportChantierRepository extends JpaRepository<RapportChantier, Long>, JpaSpecificationExecutor<RapportChantier> {
    
    List<RapportChantier> findByChefChantier(User chefChantier);
    
    List<RapportChantier> findByChantier(Chantier chantier);
    
    List<RapportChantier> findByDate(LocalDate date);
    
    Optional<RapportChantier> findByDateAndChantierAndChefChantier(
        LocalDate date, Chantier chantier, User chefChantier);
    
    @Query("SELECT r FROM RapportChantier r WHERE r.chefChantier = :chef AND r.date BETWEEN :dateDebut AND :dateFin ORDER BY r.date DESC")
    List<RapportChantier> findByChefChantierAndDateBetween(
        @Param("chef") User chef, 
        @Param("dateDebut") LocalDate dateDebut, 
        @Param("dateFin") LocalDate dateFin);
    
    @Query("SELECT r FROM RapportChantier r WHERE r.date BETWEEN :dateDebut AND :dateFin ORDER BY r.date DESC")
    List<RapportChantier> findByDateBetween(
        @Param("dateDebut") LocalDate dateDebut, 
        @Param("dateFin") LocalDate dateFin);
    
    @Query("SELECT r FROM RapportChantier r WHERE r.statut = :statut ORDER BY r.date DESC")
    List<RapportChantier> findByStatutOrderByDateDesc(@Param("statut") RapportChantier.Statut statut);
    
    @Query("SELECT COUNT(r) FROM RapportChantier r WHERE r.statut = :statut")
    long countByStatut(@Param("statut") RapportChantier.Statut statut);
    
    @Query("SELECT COUNT(r) FROM RapportChantier r WHERE r.chefChantier = :chef AND r.date BETWEEN :dateDebut AND :dateFin")
    long countByChefChantierAndDateBetween(
        @Param("chef") User chef, 
        @Param("dateDebut") LocalDate dateDebut, 
        @Param("dateFin") LocalDate dateFin);
}