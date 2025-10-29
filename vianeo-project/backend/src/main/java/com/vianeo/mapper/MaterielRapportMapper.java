package com.vianeo.mapper;

import com.vianeo.dto.MaterielRapportDTO;
import com.vianeo.entity.MaterielRapport;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MaterielRapportMapper {

    MaterielRapportDTO toDTO(MaterielRapport entity);

    @Mapping(target = "rapport", ignore = true)
    MaterielRapport toEntity(MaterielRapportDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rapport", ignore = true)
    void updateEntityFromDTO(MaterielRapportDTO dto, @MappingTarget MaterielRapport entity);
}