package com.vianeo.mapper;

import com.vianeo.dto.PersonnelRapportDTO;
import com.vianeo.entity.PersonnelRapport;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PersonnelRapportMapper {

    PersonnelRapportDTO toDTO(PersonnelRapport entity);

    @Mapping(target = "rapport", ignore = true)
    PersonnelRapport toEntity(PersonnelRapportDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rapport", ignore = true)
    void updateEntityFromDTO(PersonnelRapportDTO dto, @MappingTarget PersonnelRapport entity);
}