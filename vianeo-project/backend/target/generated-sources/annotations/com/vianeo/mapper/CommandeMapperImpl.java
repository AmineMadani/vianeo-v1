package com.vianeo.mapper;

import com.vianeo.dto.CommandeDTO;
import com.vianeo.entity.Chantier;
import com.vianeo.entity.Commande;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T18:02:02+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class CommandeMapperImpl implements CommandeMapper {

    @Override
    public CommandeDTO toDTO(Commande entity) {
        if ( entity == null ) {
            return null;
        }

        CommandeDTO commandeDTO = new CommandeDTO();

        commandeDTO.setChantierId( entityChantierId( entity ) );
        commandeDTO.setChantierNom( entityChantierNom( entity ) );
        commandeDTO.setId( entity.getId() );
        commandeDTO.setNumeroCommande( entity.getNumeroCommande() );
        commandeDTO.setDateCommande( entity.getDateCommande() );
        commandeDTO.setDateLivraison( entity.getDateLivraison() );
        commandeDTO.setDescription( entity.getDescription() );
        commandeDTO.setStatut( entity.getStatut() );
        commandeDTO.setCreatedAt( entity.getCreatedAt() );
        commandeDTO.setUpdatedAt( entity.getUpdatedAt() );

        return commandeDTO;
    }

    @Override
    public Commande toEntity(CommandeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Commande commande = new Commande();

        commande.setId( dto.getId() );
        commande.setNumeroCommande( dto.getNumeroCommande() );
        commande.setDateCommande( dto.getDateCommande() );
        commande.setDateLivraison( dto.getDateLivraison() );
        commande.setDescription( dto.getDescription() );
        commande.setStatut( dto.getStatut() );

        return commande;
    }

    @Override
    public void updateEntityFromDTO(CommandeDTO dto, Commande entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getNumeroCommande() != null ) {
            entity.setNumeroCommande( dto.getNumeroCommande() );
        }
        if ( dto.getDateCommande() != null ) {
            entity.setDateCommande( dto.getDateCommande() );
        }
        if ( dto.getDateLivraison() != null ) {
            entity.setDateLivraison( dto.getDateLivraison() );
        }
        if ( dto.getDescription() != null ) {
            entity.setDescription( dto.getDescription() );
        }
        if ( dto.getStatut() != null ) {
            entity.setStatut( dto.getStatut() );
        }
    }

    private Long entityChantierId(Commande commande) {
        if ( commande == null ) {
            return null;
        }
        Chantier chantier = commande.getChantier();
        if ( chantier == null ) {
            return null;
        }
        Long id = chantier.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityChantierNom(Commande commande) {
        if ( commande == null ) {
            return null;
        }
        Chantier chantier = commande.getChantier();
        if ( chantier == null ) {
            return null;
        }
        String nom = chantier.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }
}
