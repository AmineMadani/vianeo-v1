package com.vianeo.repository;

import com.vianeo.entity.Chantier;
import com.vianeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//public interface ChantierRepository extends JpaRepository<Chantier, Long> {
public interface ChantierRepository  extends JpaRepository<Chantier, Long>, JpaSpecificationExecutor<Chantier> {
    List<Chantier> findByStatut(Chantier.Statut statut);
    
    @Query("SELECT c FROM Chantier c JOIN c.chefs chef WHERE chef = :user")
    List<Chantier> findByChef(@Param("user") User user);
    
    @Query("SELECT c FROM Chantier c WHERE c.statut = :statut ORDER BY c.nom")
    List<Chantier> findByStatutOrderByNom(@Param("statut") Chantier.Statut statut);
    
    @Query("SELECT COUNT(c) FROM Chantier c WHERE c.statut = :statut")
    long countByStatut(@Param("statut") Chantier.Statut statut);
    
    @Query("SELECT c FROM Chantier c WHERE LOWER(c.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Chantier> findByNomContainingIgnoreCase(@Param("nom") String nom);
}