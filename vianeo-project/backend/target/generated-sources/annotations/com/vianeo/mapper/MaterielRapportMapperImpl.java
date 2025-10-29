package com.vianeo.mapper;

import com.vianeo.dto.MaterielRapportDTO;
import com.vianeo.entity.MaterielRapport;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T18:02:02+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class MaterielRapportMapperImpl implements MaterielRapportMapper {

    @Override
    public MaterielRapportDTO toDTO(MaterielRapport entity) {
        if ( entity == null ) {
            return null;
        }

        MaterielRapportDTO materielRapportDTO = new MaterielRapportDTO();

        materielRapportDTO.setId( entity.getId() );
        materielRapportDTO.setDesignation( entity.getDesignation() );
        materielRapportDTO.setType( entity.getType() );
        materielRapportDTO.setQuantite( entity.getQuantite() );
        materielRapportDTO.setUnite( entity.getUnite() );
        materielRapportDTO.setPrixUnitaire( entity.getPrixUnitaire() );
        materielRapportDTO.setFournisseur( entity.getFournisseur() );
        materielRapportDTO.setAvecChauffeur( entity.isAvecChauffeur() );

        return materielRapportDTO;
    }

    @Override
    public MaterielRapport toEntity(MaterielRapportDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MaterielRapport materielRapport = new MaterielRapport();

        materielRapport.setId( dto.getId() );
        materielRapport.setDesignation( dto.getDesignation() );
        materielRapport.setType( dto.getType() );
        materielRapport.setQuantite( dto.getQuantite() );
        materielRapport.setUnite( dto.getUnite() );
        materielRapport.setPrixUnitaire( dto.getPrixUnitaire() );
        materielRapport.setFournisseur( dto.getFournisseur() );
        materielRapport.setAvecChauffeur( dto.isAvecChauffeur() );

        return materielRapport;
    }

    @Override
    public void updateEntityFromDTO(MaterielRapportDTO dto, MaterielRapport entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getDesignation() != null ) {
            entity.setDesignation( dto.getDesignation() );
        }
        if ( dto.getType() != null ) {
            entity.setType( dto.getType() );
        }
        if ( dto.getQuantite() != null ) {
            entity.setQuantite( dto.getQuantite() );
        }
        if ( dto.getUnite() != null ) {
            entity.setUnite( dto.getUnite() );
        }
        if ( dto.getPrixUnitaire() != null ) {
            entity.setPrixUnitaire( dto.getPrixUnitaire() );
        }
        if ( dto.getFournisseur() != null ) {
            entity.setFournisseur( dto.getFournisseur() );
        }
        entity.setAvecChauffeur( dto.isAvecChauffeur() );
    }
}
