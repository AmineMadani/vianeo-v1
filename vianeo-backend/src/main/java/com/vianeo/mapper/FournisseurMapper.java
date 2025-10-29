// src/main/java/com/vianeo/mapper/FournisseurMapper.java
package com.vianeo.mapper;

import com.vianeo.dto.FournisseurDto;
import com.vianeo.model.entity.Fournisseur;

public class FournisseurMapper {

    public static FournisseurDto toDto(Fournisseur e){
        if (e == null) return null;
        return new FournisseurDto(
                e.getId(),
                e.getCode(),            // <- code en base => "nom" coté front
                e.getType(),            // <- exposé aussi en "type_fournisseur"
                e.getActif()
        );
    }

    public static Fournisseur toEntity(FournisseurDto d){
        if (d == null) return null;
        Fournisseur e = new Fournisseur();
        e.setId(d.getId());
        e.setCode(d.getCode());     // <- front envoie "nom" -> JsonAlias vers "code"
        e.setType(d.getType());     // <- alias "type_fournisseur"/"type"
        e.setActif(Boolean.TRUE.equals(d.getActif()));
        return e;
    }
}
