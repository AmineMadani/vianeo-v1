package com.vianeo.repository;

import com.vianeo.model.entity.LigneMatInterne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LigneMatInterneRepository extends JpaRepository<LigneMatInterne, Long> {
    
    List<LigneMatInterne> findByRapportIdOrderById(Long rapportId);
    
    List<LigneMatInterne> findByArticleIdOrderById(Long articleId);
    
    @Query("SELECT SUM(lmi.total) FROM LigneMatInterne lmi WHERE lmi.rapport.id = :rapportId")
    BigDecimal getTotalByRapportId(Long rapportId);
    
    void deleteByRapportId(Long rapportId);
}