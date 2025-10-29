package com.vianeo.mapper;

import com.vianeo.dto.MaterielRapportDTO;
import com.vianeo.dto.PersonnelRapportDTO;
import com.vianeo.dto.RapportChantierDTO;
import com.vianeo.entity.Chantier;
import com.vianeo.entity.MaterielRapport;
import com.vianeo.entity.PersonnelRapport;
import com.vianeo.entity.RapportChantier;
import com.vianeo.entity.User;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T18:02:02+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class RapportChantierMapperImpl implements RapportChantierMapper {

    @Autowired
    private PersonnelRapportMapper personnelRapportMapper;
    @Autowired
    private MaterielRapportMapper materielRapportMapper;

    @Override
    public RapportChantierDTO toDTO(RapportChantier entity) {
        if ( entity == null ) {
            return null;
        }

        RapportChantierDTO rapportChantierDTO = new RapportChantierDTO();

        rapportChantierDTO.setChantierId( entityChantierId( entity ) );
        rapportChantierDTO.setChantierNom( entityChantierNom( entity ) );
        rapportChantierDTO.setChefChantierId( entityChefChantierId( entity ) );
        rapportChantierDTO.setChefChantierNom( entityChefChantierNom( entity ) );
        rapportChantierDTO.setId( entity.getId() );
        rapportChantierDTO.setDate( entity.getDate() );
        rapportChantierDTO.setStatut( entity.getStatut() );
        rapportChantierDTO.setObservations( entity.getObservations() );
        rapportChantierDTO.setProblemes( entity.getProblemes() );
        rapportChantierDTO.setSecurite( entity.getSecurite() );
        rapportChantierDTO.setMeteo( entity.getMeteo() );
        rapportChantierDTO.setPersonnel( personnelRapportSetToPersonnelRapportDTOList( entity.getPersonnel() ) );
        rapportChantierDTO.setMateriel( materielRapportSetToMaterielRapportDTOList( entity.getMateriel() ) );
        rapportChantierDTO.setCreatedAt( entity.getCreatedAt() );
        rapportChantierDTO.setUpdatedAt( entity.getUpdatedAt() );

        return rapportChantierDTO;
    }

    @Override
    public RapportChantier toEntity(RapportChantierDTO dto) {
        if ( dto == null ) {
            return null;
        }

        RapportChantier rapportChantier = new RapportChantier();

        rapportChantier.setId( dto.getId() );
        rapportChantier.setDate( dto.getDate() );
        rapportChantier.setStatut( dto.getStatut() );
        rapportChantier.setObservations( dto.getObservations() );
        rapportChantier.setProblemes( dto.getProblemes() );
        rapportChantier.setSecurite( dto.getSecurite() );
        rapportChantier.setMeteo( dto.getMeteo() );

        return rapportChantier;
    }

    @Override
    public void updateEntityFromDTO(RapportChantierDTO dto, RapportChantier entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getDate() != null ) {
            entity.setDate( dto.getDate() );
        }
        if ( dto.getStatut() != null ) {
            entity.setStatut( dto.getStatut() );
        }
        if ( dto.getObservations() != null ) {
            entity.setObservations( dto.getObservations() );
        }
        if ( dto.getProblemes() != null ) {
            entity.setProblemes( dto.getProblemes() );
        }
        if ( dto.getSecurite() != null ) {
            entity.setSecurite( dto.getSecurite() );
        }
        if ( dto.getMeteo() != null ) {
            entity.setMeteo( dto.getMeteo() );
        }
        if ( entity.getPersonnel() != null ) {
            Set<PersonnelRapport> set = personnelRapportDTOListToPersonnelRapportSet( dto.getPersonnel() );
            if ( set != null ) {
                entity.getPersonnel().clear();
                entity.getPersonnel().addAll( set );
            }
        }
        else {
            Set<PersonnelRapport> set = personnelRapportDTOListToPersonnelRapportSet( dto.getPersonnel() );
            if ( set != null ) {
                entity.setPersonnel( set );
            }
        }
        if ( entity.getMateriel() != null ) {
            Set<MaterielRapport> set1 = materielRapportDTOListToMaterielRapportSet( dto.getMateriel() );
            if ( set1 != null ) {
                entity.getMateriel().clear();
                entity.getMateriel().addAll( set1 );
            }
        }
        else {
            Set<MaterielRapport> set1 = materielRapportDTOListToMaterielRapportSet( dto.getMateriel() );
            if ( set1 != null ) {
                entity.setMateriel( set1 );
            }
        }
    }

    private Long entityChantierId(RapportChantier rapportChantier) {
        if ( rapportChantier == null ) {
            return null;
        }
        Chantier chantier = rapportChantier.getChantier();
        if ( chantier == null ) {
            return null;
        }
        Long id = chantier.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityChantierNom(RapportChantier rapportChantier) {
        if ( rapportChantier == null ) {
            return null;
        }
        Chantier chantier = rapportChantier.getChantier();
        if ( chantier == null ) {
            return null;
        }
        String nom = chantier.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }

    private Long entityChefChantierId(RapportChantier rapportChantier) {
        if ( rapportChantier == null ) {
            return null;
        }
        User chefChantier = rapportChantier.getChefChantier();
        if ( chefChantier == null ) {
            return null;
        }
        Long id = chefChantier.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityChefChantierNom(RapportChantier rapportChantier) {
        if ( rapportChantier == null ) {
            return null;
        }
        User chefChantier = rapportChantier.getChefChantier();
        if ( chefChantier == null ) {
            return null;
        }
        String nom = chefChantier.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }

    protected List<PersonnelRapportDTO> personnelRapportSetToPersonnelRapportDTOList(Set<PersonnelRapport> set) {
        if ( set == null ) {
            return null;
        }

        List<PersonnelRapportDTO> list = new ArrayList<PersonnelRapportDTO>( set.size() );
        for ( PersonnelRapport personnelRapport : set ) {
            list.add( personnelRapportMapper.toDTO( personnelRapport ) );
        }

        return list;
    }

    protected List<MaterielRapportDTO> materielRapportSetToMaterielRapportDTOList(Set<MaterielRapport> set) {
        if ( set == null ) {
            return null;
        }

        List<MaterielRapportDTO> list = new ArrayList<MaterielRapportDTO>( set.size() );
        for ( MaterielRapport materielRapport : set ) {
            list.add( materielRapportMapper.toDTO( materielRapport ) );
        }

        return list;
    }

    protected Set<PersonnelRapport> personnelRapportDTOListToPersonnelRapportSet(List<PersonnelRapportDTO> list) {
        if ( list == null ) {
            return null;
        }

        Set<PersonnelRapport> set = new LinkedHashSet<PersonnelRapport>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( PersonnelRapportDTO personnelRapportDTO : list ) {
            set.add( personnelRapportMapper.toEntity( personnelRapportDTO ) );
        }

        return set;
    }

    protected Set<MaterielRapport> materielRapportDTOListToMaterielRapportSet(List<MaterielRapportDTO> list) {
        if ( list == null ) {
            return null;
        }

        Set<MaterielRapport> set = new LinkedHashSet<MaterielRapport>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( MaterielRapportDTO materielRapportDTO : list ) {
            set.add( materielRapportMapper.toEntity( materielRapportDTO ) );
        }

        return set;
    }
}
