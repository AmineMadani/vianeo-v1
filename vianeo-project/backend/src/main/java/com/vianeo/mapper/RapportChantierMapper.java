package com.vianeo.mapper;

import com.vianeo.dto.RapportChantierDTO;
import com.vianeo.entity.RapportChantier;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PersonnelRapportMapper.class, MaterielRapportMapper.class})
public interface RapportChantierMapper {

    @Mapping(source = "chantier.id", target = "chantierId")
    @Mapping(source = "chantier.nom", target = "chantierNom")
    @Mapping(source = "chefChantier.id", target = "chefChantierId")
    @Mapping(source = "chefChantier.nom", target = "chefChantierNom")
    RapportChantierDTO toDTO(RapportChantier entity);

    @Mapping(target = "chantier", ignore = true)
    @Mapping(target = "chefChantier", ignore = true)
    @Mapping(target = "personnel", ignore = true)
    @Mapping(target = "materiel", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RapportChantier toEntity(RapportChantierDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "chantier", ignore = true)
    @Mapping(target = "chefChantier", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(RapportChantierDTO dto, @MappingTarget RapportChantier entity);
}