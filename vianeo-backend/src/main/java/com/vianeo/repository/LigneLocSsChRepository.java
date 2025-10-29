package com.vianeo.repository;

import com.vianeo.model.entity.LigneLocSsCh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LigneLocSsChRepository extends JpaRepository<LigneLocSsCh, Long> {
    
    List<LigneLocSsCh> findByRapportIdOrderById(Long rapportId);
    
    List<LigneLocSsCh> findByArticleIdOrderById(Long articleId);
    
    List<LigneLocSsCh> findByFournisseurIdOrderById(Long fournisseurId);
    
    @Query("SELECT SUM(lss.total) FROM LigneLocSsCh lss WHERE lss.rapport.id = :rapportId")
    BigDecimal getTotalByRapportId(Long rapportId);
    
    void deleteByRapportId(Long rapportId);
}