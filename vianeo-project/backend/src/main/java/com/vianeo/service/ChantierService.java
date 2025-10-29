package com.vianeo.service;

import com.vianeo.dto.ChantierDTO;
import com.vianeo.entity.Chantier;
import com.vianeo.entity.User;
import com.vianeo.mapper.ChantierMapper;
import com.vianeo.repository.ChantierRepository;
import com.vianeo.repository.UserRepository;
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
public class ChantierService {

    @Autowired
    private ChantierRepository chantierRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChantierMapper chantierMapper;

    public Page<ChantierDTO> getAllChantiers(Chantier.Statut statut, String nom, Pageable pageable) {
        Specification<Chantier> spec = createSpecification(null, statut, nom);
        return chantierRepository.findAll(spec, pageable).map(chantierMapper::toDTO);
    }

    public Page<ChantierDTO> getChantiersByChef(Long chefId, Chantier.Statut statut, String nom, Pageable pageable) {
        Specification<Chantier> spec = createSpecification(chefId, statut, nom);
        return chantierRepository.findAll(spec, pageable).map(chantierMapper::toDTO);
    }

    public List<ChantierDTO> getChantiersByChefId(Long chefId) {
        User chef = userRepository.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef de chantier non trouvé"));
        
        List<Chantier> chantiers = chantierRepository.findByChef(chef);
        return chantiers.stream().map(chantierMapper::toDTO).toList();
    }

    public ChantierDTO getChantierById(Long id) {
        Chantier chantier = chantierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chantier non trouvé avec l'ID: " + id));
        return chantierMapper.toDTO(chantier);
    }

    public ChantierDTO createChantier(ChantierDTO chantierDTO) {
        Chantier chantier = chantierMapper.toEntity(chantierDTO);
        chantier = chantierRepository.save(chantier);
        return chantierMapper.toDTO(chantier);
    }

    public ChantierDTO updateChantier(Long id, ChantierDTO chantierDTO) {
        Chantier chantier = chantierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chantier non trouvé avec l'ID: " + id));
        
        chantierMapper.updateEntityFromDTO(chantierDTO, chantier);
        chantier = chantierRepository.save(chantier);
        return chantierMapper.toDTO(chantier);
    }

    public void deleteChantier(Long id) {
        Chantier chantier = chantierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chantier non trouvé avec l'ID: " + id));
        chantierRepository.delete(chantier);
    }

    public void addChefToChantier(Long chantierId, Long chefId) {
        Chantier chantier = chantierRepository.findById(chantierId)
                .orElseThrow(() -> new RuntimeException("Chantier non trouvé"));
        
        User chef = userRepository.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef de chantier non trouvé"));
        
        chantier.getChefs().add(chef);
        chantierRepository.save(chantier);
    }

    public void removeChefFromChantier(Long chantierId, Long chefId) {
        Chantier chantier = chantierRepository.findById(chantierId)
                .orElseThrow(() -> new RuntimeException("Chantier non trouvé"));
        
        User chef = userRepository.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef de chantier non trouvé"));
        
        chantier.getChefs().remove(chef);
        chantierRepository.save(chantier);
    }

    private Specification<Chantier> createSpecification(Long chefId, Chantier.Statut statut, String nom) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (chefId != null) {
                predicates.add(criteriaBuilder.isMember(
                    userRepository.findById(chefId).orElse(null), 
                    root.get("chefs")
                ));
            }
            
            if (statut != null) {
                predicates.add(criteriaBuilder.equal(root.get("statut"), statut));
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