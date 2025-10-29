package com.vianeo.service;

import com.vianeo.dto.CommandeDTO;
import com.vianeo.entity.Chantier;
import com.vianeo.entity.Commande;
import com.vianeo.mapper.CommandeMapper;
import com.vianeo.repository.ChantierRepository;
import com.vianeo.repository.CommandeRepository;
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

@Service
@Transactional
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private ChantierRepository chantierRepository;

    @Autowired
    private CommandeMapper commandeMapper;

    public Page<CommandeDTO> getAllCommandes(Long chantierId, Commande.StatutCommande statut,
                                           LocalDate dateDebut, LocalDate dateFin, Pageable pageable) {
        Specification<Commande> spec = createSpecification(chantierId, statut, dateDebut, dateFin);
        return commandeRepository.findAll(spec, pageable).map(commandeMapper::toDTO);
    }

    public List<CommandeDTO> getCommandesByChantier(Long chantierId) {
        Chantier chantier = chantierRepository.findById(chantierId)
                .orElseThrow(() -> new RuntimeException("Chantier non trouvé"));
        
        List<Commande> commandes = commandeRepository.findByChantierOrderByDateCommandeDesc(chantier);
        return commandes.stream().map(commandeMapper::toDTO).toList();
    }

    public CommandeDTO getCommandeById(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));
        return commandeMapper.toDTO(commande);
    }

    public CommandeDTO createCommande(CommandeDTO commandeDTO) {
        Commande commande = commandeMapper.toEntity(commandeDTO);
        
        Chantier chantier = chantierRepository.findById(commandeDTO.getChantierId())
                .orElseThrow(() -> new RuntimeException("Chantier non trouvé"));
        
        commande.setChantier(chantier);
        commande = commandeRepository.save(commande);
        return commandeMapper.toDTO(commande);
    }

    public CommandeDTO updateCommande(Long id, CommandeDTO commandeDTO) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));
        
        commandeMapper.updateEntityFromDTO(commandeDTO, commande);
        commande = commandeRepository.save(commande);
        return commandeMapper.toDTO(commande);
    }

    public void deleteCommande(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));
        commandeRepository.delete(commande);
    }

    public CommandeDTO updateCommandeStatus(Long id, Commande.StatutCommande statut) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));
        
        commande.setStatut(statut);
        commande = commandeRepository.save(commande);
        return commandeMapper.toDTO(commande);
    }

    private Specification<Commande> createSpecification(Long chantierId, Commande.StatutCommande statut,
                                                       LocalDate dateDebut, LocalDate dateFin) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (chantierId != null) {
                predicates.add(criteriaBuilder.equal(root.get("chantier").get("id"), chantierId));
            }
            
            if (statut != null) {
                predicates.add(criteriaBuilder.equal(root.get("statut"), statut));
            }
            
            if (dateDebut != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateCommande"), dateDebut));
            }
            
            if (dateFin != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateCommande"), dateFin));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}