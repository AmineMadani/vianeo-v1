package com.vianeo.repository;

import com.vianeo.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long>, JpaSpecificationExecutor<Fournisseur> {
    List<Fournisseur> findByType(Fournisseur.TypeFournisseur type);
    
    List<Fournisseur> findByActifTrue();
    
    @Query("SELECT f FROM Fournisseur f WHERE f.type = :type AND f.actif = true ORDER BY f.nom")
    List<Fournisseur> findByTypeAndActifTrueOrderByNom(@Param("type") Fournisseur.TypeFournisseur type);
    
    @Query("SELECT f FROM Fournisseur f WHERE LOWER(f.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Fournisseur> findByNomContainingIgnoreCase(@Param("nom") String nom);
    
    @Query("SELECT COUNT(f) FROM Fournisseur f WHERE f.type = :type AND f.actif = true")
    long countByTypeAndActifTrue(@Param("type") Fournisseur.TypeFournisseur type);
}