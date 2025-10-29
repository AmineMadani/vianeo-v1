package com.vianeo.service;

import com.vianeo.dto.FournisseurDTO;
import com.vianeo.entity.Fournisseur;
import com.vianeo.mapper.FournisseurMapper;
import com.vianeo.repository.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private FournisseurMapper fournisseurMapper;

    public Page<FournisseurDTO> getAllFournisseurs(Fournisseur.TypeFournisseur type, Boolean actif, 
                                                  String nom, Pageable pageable) {
        Specification<Fournisseur> spec = createSpecification(type, actif, nom);
        return fournisseurRepository.findAll(spec, pageable).map(fournisseurMapper::toDTO);
    }

    public List<FournisseurDTO> getActiveFournisseurs(Fournisseur.TypeFournisseur type) {
        List<Fournisseur> fournisseurs;
        if (type != null) {
            fournisseurs = fournisseurRepository.findByTypeAndActifTrueOrderByNom(type);
        } else {
            fournisseurs = fournisseurRepository.findByActifTrue();
        }
        return fournisseurs.stream().map(fournisseurMapper::toDTO).toList();
    }

    public FournisseurDTO getFournisseurById(Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé avec l'ID: " + id));
        return fournisseurMapper.toDTO(fournisseur);
    }

    public FournisseurDTO createFournisseur(FournisseurDTO fournisseurDTO) {
        Fournisseur fournisseur = fournisseurMapper.toEntity(fournisseurDTO);
        fournisseur = fournisseurRepository.save(fournisseur);
        return fournisseurMapper.toDTO(fournisseur);
    }

    public FournisseurDTO updateFournisseur(Long id, FournisseurDTO fournisseurDTO) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé avec l'ID: " + id));
        
        fournisseurMapper.updateEntityFromDTO(fournisseurDTO, fournisseur);
        fournisseur = fournisseurRepository.save(fournisseur);
        return fournisseurMapper.toDTO(fournisseur);
    }

    public void deleteFournisseur(Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé avec l'ID: " + id));
        fournisseurRepository.delete(fournisseur);
    }

    public FournisseurDTO activateFournisseur(Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé avec l'ID: " + id));
        
        fournisseur.setActif(true);
        fournisseur = fournisseurRepository.save(fournisseur);
        return fournisseurMapper.toDTO(fournisseur);
    }

    public FournisseurDTO deactivateFournisseur(Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé avec l'ID: " + id));
        
        fournisseur.setActif(false);
        fournisseur = fournisseurRepository.save(fournisseur);
        return fournisseurMapper.toDTO(fournisseur);
    }

    private Specification<Fournisseur> createSpecification(Fournisseur.TypeFournisseur type, Boolean actif, String nom) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }
            
            if (actif != null) {
                predicates.add(criteriaBuilder.equal(root.get("actif"), actif));
            }
            
            if (nom != null && !nom.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nom")), 
                    "%" + nom.toLowerCase() + "%"
                ));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}