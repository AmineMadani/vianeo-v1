package com.vianeo.mapper;

import com.vianeo.dto.FournisseurDTO;
import com.vianeo.entity.Fournisseur;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FournisseurMapper {

    FournisseurDTO toDTO(Fournisseur entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Fournisseur toEntity(FournisseurDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(FournisseurDTO dto, @MappingTarget Fournisseur entity);
}