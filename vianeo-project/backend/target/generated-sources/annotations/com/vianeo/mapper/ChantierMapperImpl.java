package com.vianeo.mapper;

import com.vianeo.dto.ChantierDTO;
import com.vianeo.dto.UserDTO;
import com.vianeo.entity.Chantier;
import com.vianeo.entity.User;
import java.util.ArrayList;
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
public class ChantierMapperImpl implements ChantierMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ChantierDTO toDTO(Chantier entity) {
        if ( entity == null ) {
            return null;
        }

        ChantierDTO chantierDTO = new ChantierDTO();

        chantierDTO.setId( entity.getId() );
        chantierDTO.setNom( entity.getNom() );
        chantierDTO.setAdresse( entity.getAdresse() );
        chantierDTO.setVille( entity.getVille() );
        chantierDTO.setCodePostal( entity.getCodePostal() );
        chantierDTO.setClient( entity.getClient() );
        chantierDTO.setDateDebut( entity.getDateDebut() );
        chantierDTO.setDateFin( entity.getDateFin() );
        chantierDTO.setStatut( entity.getStatut() );
        chantierDTO.setDescription( entity.getDescription() );
        chantierDTO.setChefs( userSetToUserDTOList( entity.getChefs() ) );
        chantierDTO.setCreatedAt( entity.getCreatedAt() );
        chantierDTO.setUpdatedAt( entity.getUpdatedAt() );

        return chantierDTO;
    }

    @Override
    public Chantier toEntity(ChantierDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Chantier chantier = new Chantier();

        chantier.setId( dto.getId() );
        chantier.setNom( dto.getNom() );
        chantier.setAdresse( dto.getAdresse() );
        chantier.setVille( dto.getVille() );
        chantier.setCodePostal( dto.getCodePostal() );
        chantier.setClient( dto.getClient() );
        chantier.setDateDebut( dto.getDateDebut() );
        chantier.setDateFin( dto.getDateFin() );
        chantier.setStatut( dto.getStatut() );
        chantier.setDescription( dto.getDescription() );

        return chantier;
    }

    @Override
    public void updateEntityFromDTO(ChantierDTO dto, Chantier entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getNom() != null ) {
            entity.setNom( dto.getNom() );
        }
        if ( dto.getAdresse() != null ) {
            entity.setAdresse( dto.getAdresse() );
        }
        if ( dto.getVille() != null ) {
            entity.setVille( dto.getVille() );
        }
        if ( dto.getCodePostal() != null ) {
            entity.setCodePostal( dto.getCodePostal() );
        }
        if ( dto.getClient() != null ) {
            entity.setClient( dto.getClient() );
        }
        if ( dto.getDateDebut() != null ) {
            entity.setDateDebut( dto.getDateDebut() );
        }
        if ( dto.getDateFin() != null ) {
            entity.setDateFin( dto.getDateFin() );
        }
        if ( dto.getStatut() != null ) {
            entity.setStatut( dto.getStatut() );
        }
        if ( dto.getDescription() != null ) {
            entity.setDescription( dto.getDescription() );
        }
    }

    protected List<UserDTO> userSetToUserDTOList(Set<User> set) {
        if ( set == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( set.size() );
        for ( User user : set ) {
            list.add( userMapper.toDTO( user ) );
        }

        return list;
    }
}
