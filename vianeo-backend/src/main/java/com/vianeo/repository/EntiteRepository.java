// src/main/java/com/vianeo/repository/EntiteRepository.java
package com.vianeo.repository;

import com.vianeo.model.entity.Entite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntiteRepository extends JpaRepository<Entite, Long> {
    // Ajoute ici des méthodes dérivées si tu en as besoin plus tard, par ex. :
    // List<Entite> findByActifTrueOrderByNomAsc();
    // boolean existsBySiret(String siret);
}
