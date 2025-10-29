package com.vianeo.mapper;

import com.vianeo.dto.ChantierDTO;
import com.vianeo.entity.Chantier;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ChantierMapper {

    ChantierDTO toDTO(Chantier entity);

    @Mapping(target = "chefs", ignore = true)
    @Mapping(target = "rapports", ignore = true)
    @Mapping(target = "commandes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Chantier toEntity(ChantierDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "chefs", ignore = true)
    @Mapping(target = "rapports", ignore = true)
    @Mapping(target = "commandes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(ChantierDTO dto, @MappingTarget Chantier entity);
}