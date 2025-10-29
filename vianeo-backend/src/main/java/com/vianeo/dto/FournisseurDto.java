package com.vianeo.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vianeo.model.enums.TypeFournisseur;

public class FournisseurDto {

    private Long id;

    // Le front envoie/reçoit "nom" ; en base c’est "code"
    @JsonAlias({"nom","code"})
    private String code;

    // Le front envoie "type_fournisseur" ; accepte aussi "type"
    @JsonAlias({"type_fournisseur","type"})
    private TypeFournisseur type;

    private Boolean actif;

    public FournisseurDto() {}  // no-args

    public FournisseurDto(Long id, String code, TypeFournisseur type, Boolean actif) {
        this.id = id;
        this.code = code;
        this.type = type;
        this.actif = actif;
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public TypeFournisseur getType() { return type; }
    public void setType(TypeFournisseur type) { this.type = type; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }

    // Exposer aussi les noms attendus par le front
    @JsonProperty("nom")
    public String getNom() { return code; }

    @JsonProperty("type_fournisseur")
    public TypeFournisseur getTypeFournisseur() { return type; }
}
