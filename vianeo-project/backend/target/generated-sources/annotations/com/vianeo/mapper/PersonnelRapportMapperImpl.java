package com.vianeo.mapper;

import com.vianeo.dto.PersonnelRapportDTO;
import com.vianeo.entity.PersonnelRapport;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T18:02:01+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class PersonnelRapportMapperImpl implements PersonnelRapportMapper {

    @Override
    public PersonnelRapportDTO toDTO(PersonnelRapport entity) {
        if ( entity == null ) {
            return null;
        }

        PersonnelRapportDTO personnelRapportDTO = new PersonnelRapportDTO();

        personnelRapportDTO.setId( entity.getId() );
        personnelRapportDTO.setNom( entity.getNom() );
        personnelRapportDTO.setPrenom( entity.getPrenom() );
        personnelRapportDTO.setType( entity.getType() );
        personnelRapportDTO.setHeuresLundi( entity.getHeuresLundi() );
        personnelRapportDTO.setHeuresMardi( entity.getHeuresMardi() );
        personnelRapportDTO.setHeuresMercredi( entity.getHeuresMercredi() );
        personnelRapportDTO.setHeuresJeudi( entity.getHeuresJeudi() );
        personnelRapportDTO.setHeuresVendredi( entity.getHeuresVendredi() );
        personnelRapportDTO.setHeuresSamedi( entity.getHeuresSamedi() );
        personnelRapportDTO.setTauxJournalier( entity.getTauxJournalier() );
        personnelRapportDTO.setFournisseur( entity.getFournisseur() );

        return personnelRapportDTO;
    }

    @Override
    public PersonnelRapport toEntity(PersonnelRapportDTO dto) {
        if ( dto == null ) {
            return null;
        }

        PersonnelRapport personnelRapport = new PersonnelRapport();

        personnelRapport.setId( dto.getId() );
        personnelRapport.setNom( dto.getNom() );
        personnelRapport.setPrenom( dto.getPrenom() );
        personnelRapport.setType( dto.getType() );
        personnelRapport.setHeuresLundi( dto.getHeuresLundi() );
        personnelRapport.setHeuresMardi( dto.getHeuresMardi() );
        personnelRapport.setHeuresMercredi( dto.getHeuresMercredi() );
        personnelRapport.setHeuresJeudi( dto.getHeuresJeudi() );
        personnelRapport.setHeuresVendredi( dto.getHeuresVendredi() );
        personnelRapport.setHeuresSamedi( dto.getHeuresSamedi() );
        personnelRapport.setTauxJournalier( dto.getTauxJournalier() );
        personnelRapport.setFournisseur( dto.getFournisseur() );

        return personnelRapport;
    }

    @Override
    public void updateEntityFromDTO(PersonnelRapportDTO dto, PersonnelRapport entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getNom() != null ) {
            entity.setNom( dto.getNom() );
        }
        if ( dto.getPrenom() != null ) {
            entity.setPrenom( dto.getPrenom() );
        }
        if ( dto.getType() != null ) {
            entity.setType( dto.getType() );
        }
        if ( dto.getHeuresLundi() != null ) {
            entity.setHeuresLundi( dto.getHeuresLundi() );
        }
        if ( dto.getHeuresMardi() != null ) {
            entity.setHeuresMardi( dto.getHeuresMardi() );
        }
        if ( dto.getHeuresMercredi() != null ) {
            entity.setHeuresMercredi( dto.getHeuresMercredi() );
        }
        if ( dto.getHeuresJeudi() != null ) {
            entity.setHeuresJeudi( dto.getHeuresJeudi() );
        }
        if ( dto.getHeuresVendredi() != null ) {
            entity.setHeuresVendredi( dto.getHeuresVendredi() );
        }
        if ( dto.getHeuresSamedi() != null ) {
            entity.setHeuresSamedi( dto.getHeuresSamedi() );
        }
        if ( dto.getTauxJournalier() != null ) {
            entity.setTauxJournalier( dto.getTauxJournalier() );
        }
        if ( dto.getFournisseur() != null ) {
            entity.setFournisseur( dto.getFournisseur() );
        }
    }
}
