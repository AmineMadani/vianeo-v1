package com.vianeo.repository;

import com.vianeo.model.entity.LignePrestaExt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LignePrestaExtRepository extends JpaRepository<LignePrestaExt, Long> {
    
    List<LignePrestaExt> findByRapportIdOrderById(Long rapportId);
    
    List<LignePrestaExt> findByArticleIdOrderById(Long articleId);
    
    List<LignePrestaExt> findByFournisseurIdOrderById(Long fournisseurId);
    
    @Query("SELECT SUM(lpe.total) FROM LignePrestaExt lpe WHERE lpe.rapport.id = :rapportId")
    BigDecimal getTotalByRapportId(Long rapportId);
    
    void deleteByRapportId(Long rapportId);
}