package com.vianeo.repository;

import com.vianeo.dto.RapportLite;
import com.vianeo.model.entity.Rapport;
import com.vianeo.model.enums.StatutRapport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RapportRepository extends JpaRepository<Rapport, Long> {
    
    Optional<Rapport> findByChantierIdAndJour(Long chantierId, LocalDate jour);
    
    List<Rapport> findByChantierIdOrderByJourDesc(Long chantierId);
    
    List<Rapport> findByStatut(StatutRapport statut);
    
    @Query("SELECT r FROM Rapport r WHERE r.chantier.id = :chantierId " +
           "AND r.jour BETWEEN :dateDebut AND :dateFin ORDER BY r.jour DESC")
    List<Rapport> findByChantierAndPeriod(@Param("chantierId") Long chantierId, 
                                         @Param("dateDebut") LocalDate dateDebut,
                                         @Param("dateFin") LocalDate dateFin);
    
    @Query("SELECT r FROM Rapport r WHERE r.auteur.id = :auteurId ORDER BY r.jour DESC")
    List<Rapport> findByAuteurIdOrderByJourDesc(Long auteurId);
    
    @Query("SELECT COUNT(r) FROM Rapport r WHERE r.chantier.id = :chantierId AND r.statut = :statut")
    long countByChantierIdAndStatut(Long chantierId, StatutRapport statut);


    }

