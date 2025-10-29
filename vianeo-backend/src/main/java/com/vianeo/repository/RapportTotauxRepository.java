package com.vianeo.repository;

import com.vianeo.model.entity.RapportTotalEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface RapportTotauxRepository extends CrudRepository<RapportTotalEntity, Long> {

    @Query(value = "SELECT total_general FROM vianeo.v_rapport_totaux WHERE rapport_id = :rapportId", nativeQuery = true)
    Optional<BigDecimal> findTotalByRapportId(Long rapportId);
}
