package com.vianeo.service;

import com.vianeo.dto.RapportChantierDTO;
import com.vianeo.entity.Chantier;
import com.vianeo.entity.RapportChantier;
import com.vianeo.entity.User;
import com.vianeo.mapper.RapportChantierMapper;
import com.vianeo.repository.ChantierRepository;
import com.vianeo.repository.RapportChantierRepository;
import com.vianeo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RapportChantierService {

    @Autowired
    private RapportChantierRepository rapportRepository;

    @Autowired
    private ChantierRepository chantierRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RapportChantierMapper rapportMapper;

    public Page<RapportChantierDTO> getAllRapports(LocalDate dateDebut, LocalDate dateFin, 
                                                  Long chantierId, RapportChantier.Statut statut, 
                                                  Pageable pageable) {
        Specification<RapportChantier> spec = createSpecification(null, dateDebut, dateFin, chantierId, statut);
        return rapportRepository.findAll(spec, pageable).map(rapportMapper::toDTO);
    }

    public Page<RapportChantierDTO> getRapportsByChef(Long chefId, LocalDate dateDebut, LocalDate dateFin,
                                                     Long chantierId, RapportChantier.Statut statut,
                                                     Pageable pageable) {
        Specification<RapportChantier> spec = createSpecification(chefId, dateDebut, dateFin, chantierId, statut);
        return rapportRepository.findAll(spec, pageable).map(rapportMapper::toDTO);
    }

    public RapportChantierDTO getRapportById(Long id, Long chefId) {
        RapportChantier rapport = rapportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé avec l'ID: " + id));
        
        // Vérifier que le chef peut accéder à ce rapport
        if (chefId != null && !rapport.getChefChantier().getId().equals(chefId)) {
            throw new RuntimeException("Accès non autorisé à ce rapport");
        }
        
        return rapportMapper.toDTO(rapport);
    }

    public RapportChantierDTO createRapport(RapportChantierDTO rapportDTO) {
        RapportChantier rapport = rapportMapper.toEntity(rapportDTO);
        
        // Vérifier que le chantier existe
        Chantier chantier = chantierRepository.findById(rapportDTO.getChantierId())
                .orElseThrow(() -> new RuntimeException("Chantier non trouvé"));
        
        // Vérifier que le chef existe
        User chef = userRepository.findById(rapportDTO.getChefChantierId())
                .orElseThrow(() -> new RuntimeException("Chef de chantier non trouvé"));
        
        rapport.setChantier(chantier);
        rapport.setChefChantier(chef);
        
        // Vérifier qu'il n'existe pas déjà un rapport pour cette date/chantier/chef
        Optional<RapportChantier> existingRapport = rapportRepository
                .findByDateAndChantierAndChefChantier(rapport.getDate(), chantier, chef);
        
        if (existingRapport.isPresent()) {
            throw new RuntimeException("Un rapport existe déjà pour cette date et ce chantier");
        }
        
        rapport = rapportRepository.save(rapport);
        return rapportMapper.toDTO(rapport);
    }

    public RapportChantierDTO updateRapport(Long id, RapportChantierDTO rapportDTO, Long chefId) {
        RapportChantier rapport = rapportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé avec l'ID: " + id));
        
        // Vérifier que le chef peut modifier ce rapport
        if (chefId != null && !rapport.getChefChantier().getId().equals(chefId)) {
            throw new RuntimeException("Accès non autorisé à ce rapport");
        }
        
        // Ne pas permettre la modification d'un rapport validé
        if (rapport.getStatut() == RapportChantier.Statut.VALIDE) {
            throw new RuntimeException("Impossible de modifier un rapport validé");
        }
        
        rapportMapper.updateEntityFromDTO(rapportDTO, rapport);
        rapport = rapportRepository.save(rapport);
        return rapportMapper.toDTO(rapport);
    }

    public RapportChantierDTO submitRapport(Long id, Long chefId) {
        RapportChantier rapport = rapportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé avec l'ID: " + id));
        
        if (chefId != null && !rapport.getChefChantier().getId().equals(chefId)) {
            throw new RuntimeException("Accès non autorisé à ce rapport");
        }
        
        rapport.setStatut(RapportChantier.Statut.COMPLET);
        rapport = rapportRepository.save(rapport);
        return rapportMapper.toDTO(rapport);
    }

    public RapportChantierDTO validateRapport(Long id) {
        RapportChantier rapport = rapportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé avec l'ID: " + id));
        
        rapport.setStatut(RapportChantier.Statut.VALIDE);
        rapport = rapportRepository.save(rapport);
        return rapportMapper.toDTO(rapport);
    }

    public void deleteRapport(Long id, Long chefId) {
        RapportChantier rapport = rapportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé avec l'ID: " + id));
        
        if (chefId != null && !rapport.getChefChantier().getId().equals(chefId)) {
            throw new RuntimeException("Accès non autorisé à ce rapport");
        }
        
        if (rapport.getStatut() == RapportChantier.Statut.VALIDE) {
            throw new RuntimeException("Impossible de supprimer un rapport validé");
        }
        
        rapportRepository.delete(rapport);
    }

    public RapportChantierDTO getTodayRapport(Long chantierId, Long chefId) {
        Chantier chantier = chantierRepository.findById(chantierId)
                .orElseThrow(() -> new RuntimeException("Chantier non trouvé"));
        
        User chef = userRepository.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef de chantier non trouvé"));
        
        Optional<RapportChantier> rapport = rapportRepository
                .findByDateAndChantierAndChefChantier(LocalDate.now(), chantier, chef);
        
        if (rapport.isPresent()) {
            return rapportMapper.toDTO(rapport.get());
        } else {
            // Créer un nouveau rapport pour aujourd'hui
            RapportChantierDTO newRapport = new RapportChantierDTO();
            newRapport.setDate(LocalDate.now());
            newRapport.setChantierId(chantierId);
            newRapport.setChefChantierId(chefId);
            return createRapport(newRapport);
        }
    }

    public RapportChantierDTO duplicateRapport(Long id, LocalDate newDate, Long chefId) {
        RapportChantier originalRapport = rapportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé avec l'ID: " + id));
        
        if (chefId != null && !originalRapport.getChefChantier().getId().equals(chefId)) {
            throw new RuntimeException("Accès non autorisé à ce rapport");
        }
        
        // Vérifier qu'il n'existe pas déjà un rapport pour la nouvelle date
        Optional<RapportChantier> existingRapport = rapportRepository
                .findByDateAndChantierAndChefChantier(newDate, originalRapport.getChantier(), originalRapport.getChefChantier());
        
        if (existingRapport.isPresent()) {
            throw new RuntimeException("Un rapport existe déjà pour cette date");
        }
        
        RapportChantierDTO originalDTO = rapportMapper.toDTO(originalRapport);
        originalDTO.setId(null);
        originalDTO.setDate(newDate);
        originalDTO.setStatut(RapportChantier.Statut.BROUILLON);
        originalDTO.setCreatedAt(null);
        originalDTO.setUpdatedAt(null);
        
        return createRapport(originalDTO);
    }

    private Specification<RapportChantier> createSpecification(Long chefId, LocalDate dateDebut, LocalDate dateFin,
                                                              Long chantierId, RapportChantier.Statut statut) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (chefId != null) {
                predicates.add(criteriaBuilder.equal(root.get("chefChantier").get("id"), chefId));
            }
            
            if (dateDebut != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), dateDebut));
            }
            
            if (dateFin != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), dateFin));
            }
            
            if (chantierId != null) {
                predicates.add(criteriaBuilder.equal(root.get("chantier").get("id"), chantierId));
            }
            
            if (statut != null) {
                predicates.add(criteriaBuilder.equal(root.get("statut"), statut));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}