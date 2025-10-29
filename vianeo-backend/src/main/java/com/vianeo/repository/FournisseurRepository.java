// com.vianeo.repository.FournisseurRepository
package com.vianeo.repository;

import com.vianeo.model.entity.Fournisseur;
import com.vianeo.model.enums.TypeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    List<Fournisseur> findAllByOrderByCode();

    List<Fournisseur> findByActifTrueOrderByCode();

    List<Fournisseur> findByActifOrderByCode(Boolean actif);

    List<Fournisseur> findByTypeAndActifTrueOrderByCode(TypeFournisseur type);

    List<Fournisseur> findByTypeAndActifOrderByCode(TypeFournisseur type, Boolean actif);

    Optional<Fournisseur> findByCode(String code); // utile pour éviter les doublons
}
