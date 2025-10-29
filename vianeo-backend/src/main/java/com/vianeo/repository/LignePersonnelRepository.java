package com.vianeo.repository;

import com.vianeo.model.entity.LignePersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LignePersonnelRepository extends JpaRepository<LignePersonnel, Long> {
    
    List<LignePersonnel> findByRapportIdOrderById(Long rapportId);
    
    List<LignePersonnel> findByUtilisateurIdOrderById(Long utilisateurId);
    
    @Query("SELECT SUM(lp.total) FROM LignePersonnel lp WHERE lp.rapport.id = :rapportId")
    BigDecimal getTotalByRapportId(Long rapportId);
    
    void deleteByRapportId(Long rapportId);

    boolean existsByRapportId(Long rapportId);
}