package com.vianeo.mapper;

import com.vianeo.dto.CommandeDTO;
import com.vianeo.entity.Commande;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CommandeMapper {

    @Mapping(source = "chantier.id", target = "chantierId")
    @Mapping(source = "chantier.nom", target = "chantierNom")
    CommandeDTO toDTO(Commande entity);

    @Mapping(target = "chantier", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Commande toEntity(CommandeDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "chantier", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(CommandeDTO dto, @MappingTarget Commande entity);
}