package com.vianeo.repository;

import com.vianeo.model.entity.Chantier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChantierRepository extends JpaRepository<Chantier, Long> {
    
    List<Chantier> findByActifTrue();
    
    Optional<Chantier> findByCode(String code);
    
    @Query("SELECT c FROM Chantier c WHERE c.actif = true ORDER BY c.libelle")
    List<Chantier> findAllActiveOrderByLibelle();
    
    @Query("SELECT c FROM Chantier c WHERE c.entite.id = :entiteId AND c.actif = true")
    List<Chantier> findByEntiteIdAndActifTrue(Long entiteId);
}