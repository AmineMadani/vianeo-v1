package com.vianeo.repository;

import com.vianeo.model.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
    
    Optional<Compte> findByUsername(String username);
    
    Optional<Compte> findByEmail(String email);
    
    @Query("SELECT c FROM Compte c JOIN FETCH c.utilisateur WHERE c.username = :username AND c.actif = true")
    Optional<Compte> findByUsernameAndActifTrue(String username);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}