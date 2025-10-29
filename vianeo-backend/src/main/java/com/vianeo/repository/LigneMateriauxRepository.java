package com.vianeo.repository;

import com.vianeo.model.entity.LigneMateriaux;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LigneMateriauxRepository extends JpaRepository<LigneMateriaux, Long> {
    
    List<LigneMateriaux> findByRapportIdOrderById(Long rapportId);
    
    List<LigneMateriaux> findByArticleIdOrderById(Long articleId);
    
    List<LigneMateriaux> findByFournisseurIdOrderById(Long fournisseurId);
    
    @Query("SELECT SUM(lm.total) FROM LigneMateriaux lm WHERE lm.rapport.id = :rapportId")
    BigDecimal getTotalByRapportId(Long rapportId);
    
    void deleteByRapportId(Long rapportId);
}