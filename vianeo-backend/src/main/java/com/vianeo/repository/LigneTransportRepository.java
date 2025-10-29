package com.vianeo.repository;

import com.vianeo.model.entity.LigneTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LigneTransportRepository extends JpaRepository<LigneTransport, Long> {
    
    List<LigneTransport> findByRapportIdOrderById(Long rapportId);
    
    List<LigneTransport> findByArticleIdOrderById(Long articleId);
    
    List<LigneTransport> findByFournisseurIdOrderById(Long fournisseurId);
    
    @Query("SELECT SUM(lt.total) FROM LigneTransport lt WHERE lt.rapport.id = :rapportId")
    BigDecimal getTotalByRapportId(Long rapportId);
    
    void deleteByRapportId(Long rapportId);
}