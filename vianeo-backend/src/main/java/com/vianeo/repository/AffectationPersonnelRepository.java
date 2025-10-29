package com.vianeo.repository;

import com.vianeo.model.entity.AffectationPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AffectationPersonnelRepository extends JpaRepository<AffectationPersonnel, Long> {

    List<AffectationPersonnel> findByChantierIdOrderByDateDebut(Long chantierId);

    List<AffectationPersonnel> findByPersonnelIdOrderByDateDebut(Long personnelId);

    @Query("""
           SELECT ap FROM AffectationPersonnel ap
           WHERE ap.chantier.id = :chantierId
             AND (ap.dateFin IS NULL OR ap.dateFin >= :date)
             AND ap.dateDebut <= :date
           """)
    List<AffectationPersonnel> findActiveAffectationsForChantierAtDate(
            @Param("chantierId") Long chantierId,
            @Param("date") LocalDate date
    );

    @Query("""
           SELECT ap FROM AffectationPersonnel ap
           WHERE ap.personnel.id = :personnelId
             AND (ap.dateFin IS NULL OR ap.dateFin >= :date)
             AND ap.dateDebut <= :date
           """)
    List<AffectationPersonnel> findActiveAffectationsForPersonnelAtDate(
            @Param("personnelId") Long personnelId,
            @Param("date") LocalDate date
    );
}
