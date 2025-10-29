package com.vianeo.service;

import com.vianeo.dto.EntiteDTO;
import com.vianeo.entity.Entite;
import com.vianeo.mapper.EntiteMapper;
import com.vianeo.repository.EntiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EntiteService {

    @Autowired
    private EntiteRepository entiteRepository;

    @Autowired
    private EntiteMapper entiteMapper;

    public List<EntiteDTO> getAllEntites() {
        List<Entite> entites = entiteRepository.findAllByOrderByNom();
        return entites.stream().map(entiteMapper::toDTO).toList();
    }

    public EntiteDTO getEntiteById(Long id) {
        Entite entite = entiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entité non trouvée avec l'ID: " + id));
        return entiteMapper.toDTO(entite);
    }

    public EntiteDTO createEntite(EntiteDTO entiteDTO) {
        Entite entite = entiteMapper.toEntity(entiteDTO);
        entite = entiteRepository.save(entite);
        return entiteMapper.toDTO(entite);
    }

    public EntiteDTO updateEntite(Long id, EntiteDTO entiteDTO) {
        Entite entite = entiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entité non trouvée avec l'ID: " + id));
        
        entiteMapper.updateEntityFromDTO(entiteDTO, entite);
        entite = entiteRepository.save(entite);
        return entiteMapper.toDTO(entite);
    }

    public void deleteEntite(Long id) {
        Entite entite = entiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entité non trouvée avec l'ID: " + id));
        entiteRepository.delete(entite);
    }
}