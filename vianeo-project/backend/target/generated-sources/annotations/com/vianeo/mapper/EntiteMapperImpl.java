package com.vianeo.mapper;

import com.vianeo.dto.EntiteDTO;
import com.vianeo.entity.Entite;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T18:02:02+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class EntiteMapperImpl implements EntiteMapper {

    @Override
    public EntiteDTO toDTO(Entite entity) {
        if ( entity == null ) {
            return null;
        }

        EntiteDTO entiteDTO = new EntiteDTO();

        entiteDTO.setId( entity.getId() );
        entiteDTO.setNom( entity.getNom() );
        entiteDTO.setAdresse( entity.getAdresse() );
        entiteDTO.setVille( entity.getVille() );
        entiteDTO.setCodePostal( entity.getCodePostal() );
        entiteDTO.setTelephone( entity.getTelephone() );
        entiteDTO.setEmail( entity.getEmail() );
        entiteDTO.setSiret( entity.getSiret() );
        entiteDTO.setCreatedAt( entity.getCreatedAt() );
        entiteDTO.setUpdatedAt( entity.getUpdatedAt() );

        return entiteDTO;
    }

    @Override
    public Entite toEntity(EntiteDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Entite entite = new Entite();

        entite.setId( dto.getId() );
        entite.setNom( dto.getNom() );
        entite.setAdresse( dto.getAdresse() );
        entite.setVille( dto.getVille() );
        entite.setCodePostal( dto.getCodePostal() );
        entite.setTelephone( dto.getTelephone() );
        entite.setEmail( dto.getEmail() );
        entite.setSiret( dto.getSiret() );

        return entite;
    }

    @Override
    public void updateEntityFromDTO(EntiteDTO dto, Entite entity) {
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
        if ( dto.getTelephone() != null ) {
            entity.setTelephone( dto.getTelephone() );
        }
        if ( dto.getEmail() != null ) {
            entity.setEmail( dto.getEmail() );
        }
        if ( dto.getSiret() != null ) {
            entity.setSiret( dto.getSiret() );
        }
    }
}
