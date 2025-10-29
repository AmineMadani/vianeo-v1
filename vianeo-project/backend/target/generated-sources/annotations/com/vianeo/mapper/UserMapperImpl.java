package com.vianeo.mapper;

import com.vianeo.dto.UserDTO;
import com.vianeo.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T18:02:02+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( entity.getId() );
        userDTO.setNom( entity.getNom() );
        userDTO.setPrenom( entity.getPrenom() );
        userDTO.setEmail( entity.getEmail() );
        userDTO.setTelephone( entity.getTelephone() );
        userDTO.setRole( entity.getRole() );
        userDTO.setActive( entity.isActive() );
        userDTO.setEmailVerified( entity.isEmailVerified() );
        userDTO.setCreatedAt( entity.getCreatedAt() );
        userDTO.setUpdatedAt( entity.getUpdatedAt() );

        return userDTO;
    }

    @Override
    public User toEntity(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setId( dto.getId() );
        user.setNom( dto.getNom() );
        user.setPrenom( dto.getPrenom() );
        user.setEmail( dto.getEmail() );
        user.setTelephone( dto.getTelephone() );
        user.setRole( dto.getRole() );
        user.setActive( dto.isActive() );
        user.setEmailVerified( dto.isEmailVerified() );

        return user;
    }

    @Override
    public void updateEntityFromDTO(UserDTO dto, User entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getNom() != null ) {
            entity.setNom( dto.getNom() );
        }
        if ( dto.getPrenom() != null ) {
            entity.setPrenom( dto.getPrenom() );
        }
        if ( dto.getEmail() != null ) {
            entity.setEmail( dto.getEmail() );
        }
        if ( dto.getTelephone() != null ) {
            entity.setTelephone( dto.getTelephone() );
        }
        if ( dto.getRole() != null ) {
            entity.setRole( dto.getRole() );
        }
        entity.setActive( dto.isActive() );
        entity.setEmailVerified( dto.isEmailVerified() );
    }
}
