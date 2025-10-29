package com.vianeo.repository;

import com.vianeo.model.entity.LigneInterim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LigneInterimRepository extends JpaRepository<LigneInterim, Long> {
    
    List<LigneInterim> findByRapportIdOrderById(Long rapportId);
    
    List<LigneInterim> findByFournisseurIdOrderById(Long fournisseurId);
    
    @Query("SELECT SUM(li.total) FROM LigneInterim li WHERE li.rapport.id = :rapportId")
    BigDecimal getTotalByRapportId(Long rapportId);
    
    void deleteByRapportId(Long rapportId);

    boolean existsByRapportId(Long rapportId);

}