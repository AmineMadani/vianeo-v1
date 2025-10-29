package com.vianeo.repository;

import com.vianeo.entity.Entite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntiteRepository extends JpaRepository<Entite, Long> {
    
    List<Entite> findAllByOrderByNom();
    
    List<Entite> findByNomContainingIgnoreCase(String nom);
}