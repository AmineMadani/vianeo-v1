package com.vianeo.dto.rapport;

import jakarta.validation.constraints.NotBlank;

public class RefuserRapportRequest {
    
    @NotBlank(message = "Le motif de refus est obligatoire")
    private String motif;

    // Constructors
    public RefuserRapportRequest() {}

    public RefuserRapportRequest(String motif) {
        this.motif = motif;
    }

    // Getters and Setters
    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
}