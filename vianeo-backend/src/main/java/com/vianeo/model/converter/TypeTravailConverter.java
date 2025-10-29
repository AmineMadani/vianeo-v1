package com.vianeo.model.converter;

import com.vianeo.model.enums.TypeTravail;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypeTravailConverter implements AttributeConverter<TypeTravail, String> {

    @Override
    public String convertToDatabaseColumn(TypeTravail attribute) {
        return (attribute == null) ? null : attribute.getCode(); // ← écrit 'J' 'N' ou 'GD'
    }

    @Override
    public TypeTravail convertToEntityAttribute(String dbValue) {
        return (dbValue == null) ? null : TypeTravail.fromString(dbValue);
    }
}
