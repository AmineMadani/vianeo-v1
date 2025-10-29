package com.vianeo.mapper;

import com.vianeo.dto.FournisseurDTO;
import com.vianeo.entity.Fournisseur;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T18:02:02+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class FournisseurMapperImpl implements FournisseurMapper {

    @Override
    public FournisseurDTO toDTO(Fournisseur entity) {
        if ( entity == null ) {
            return null;
        }

        FournisseurDTO fournisseurDTO = new FournisseurDTO();

        fournisseurDTO.setId( entity.getId() );
        fournisseurDTO.setNom( entity.getNom() );
        fournisseurDTO.setType( entity.getType() );
        fournisseurDTO.setAdresse( entity.getAdresse() );
        fournisseurDTO.setVille( entity.getVille() );
        fournisseurDTO.setCodePostal( entity.getCodePostal() );
        fournisseurDTO.setTelephone( entity.getTelephone() );
        fournisseurDTO.setEmail( entity.getEmail() );
        fournisseurDTO.setContactPrincipal( entity.getContactPrincipal() );
        fournisseurDTO.setActif( entity.isActif() );
        fournisseurDTO.setCreatedAt( entity.getCreatedAt() );
        fournisseurDTO.setUpdatedAt( entity.getUpdatedAt() );

        return fournisseurDTO;
    }

    @Override
    public Fournisseur toEntity(FournisseurDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Fournisseur fournisseur = new Fournisseur();

        fournisseur.setId( dto.getId() );
        fournisseur.setNom( dto.getNom() );
        fournisseur.setType( dto.getType() );
        fournisseur.setAdresse( dto.getAdresse() );
        fournisseur.setVille( dto.getVille() );
        fournisseur.setCodePostal( dto.getCodePostal() );
        fournisseur.setTelephone( dto.getTelephone() );
        fournisseur.setEmail( dto.getEmail() );
        fournisseur.setContactPrincipal( dto.getContactPrincipal() );
        fournisseur.setActif( dto.isActif() );

        return fournisseur;
    }

    @Override
    public void updateEntityFromDTO(FournisseurDTO dto, Fournisseur entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getNom() != null ) {
            entity.setNom( dto.getNom() );
        }
        if ( dto.getType() != null ) {
            entity.setType( dto.getType() );
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
        if ( dto.getContactPrincipal() != null ) {
            entity.setContactPrincipal( dto.getContactPrincipal() );
        }
        entity.setActif( dto.isActif() );
    }
}
