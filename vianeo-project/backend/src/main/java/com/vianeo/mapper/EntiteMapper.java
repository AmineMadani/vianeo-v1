package com.vianeo.mapper;

import com.vianeo.dto.EntiteDTO;
import com.vianeo.entity.Entite;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EntiteMapper {

    EntiteDTO toDTO(Entite entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Entite toEntity(EntiteDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(EntiteDTO dto, @MappingTarget Entite entity);
}