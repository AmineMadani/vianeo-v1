package com.vianeo.repository;

import com.vianeo.model.entity.LigneLocAvecCh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LigneLocAvecChRepository extends JpaRepository<LigneLocAvecCh, Long> {
    
    List<LigneLocAvecCh> findByRapportIdOrderById(Long rapportId);
    
    List<LigneLocAvecCh> findByArticleIdOrderById(Long articleId);
    
    List<LigneLocAvecCh> findByFournisseurIdOrderById(Long fournisseurId);
    
    @Query("SELECT SUM(lac.total) FROM LigneLocAvecCh lac WHERE lac.rapport.id = :rapportId")
    BigDecimal getTotalByRapportId(Long rapportId);
    
    void deleteByRapportId(Long rapportId);
}