package com.vianeo.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TypeTravail {
    JOUR("J"),
    NUIT("N"),
    GRAND_DEPLACEMENT("GD");

    private final String code;
    TypeTravail(String code) { this.code = code; }
    public String getCode() { return code; }

    @JsonCreator
    public static TypeTravail fromString(String value) {
        if (value == null) return null;
        String v = value.trim().toUpperCase();
        return switch (v) {
            case "J", "JOUR" -> JOUR;
            case "N", "NUIT" -> NUIT;
            case "GD", "GRAND_DEPLACEMENT" -> GRAND_DEPLACEMENT;
            default -> throw new IllegalArgumentException("TypeTravail inconnu : " + value);
        };
    }
}
