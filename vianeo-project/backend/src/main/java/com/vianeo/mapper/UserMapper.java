package com.vianeo.mapper;

import com.vianeo.dto.UserDTO;
import com.vianeo.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "verificationToken", ignore = true)
    @Mapping(target = "rapports", ignore = true)
    @Mapping(target = "chantiers", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "verificationToken", ignore = true)
    @Mapping(target = "rapports", ignore = true)
    @Mapping(target = "chantiers", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(UserDTO dto, @MappingTarget User entity);
}