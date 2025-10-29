package com.vianeo.repository;

import com.vianeo.model.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    
    List<Utilisateur> findByActifTrue();
    
    List<Utilisateur> findByProfilAndActifTrue(String profil);
    
    @Query("SELECT u FROM Utilisateur u WHERE u.entite.id = :entiteId AND u.actif = true")
    List<Utilisateur> findByEntiteIdAndActifTrue(Long entiteId);
    
    @Query("SELECT u FROM Utilisateur u WHERE u.actif = true ORDER BY u.nom, u.prenom")
    List<Utilisateur> findAllActiveOrderByNomPrenom();

    List<Utilisateur> findAllByOrderByNomAscPrenomAsc();

}