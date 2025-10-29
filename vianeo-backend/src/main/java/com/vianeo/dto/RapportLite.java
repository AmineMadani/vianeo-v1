package com.vianeo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RapportLite {
    private Long id;
    private LocalDate jour;
    private Long chantierId;
    private String chantierCode;
    private String chantierLibelle;
    private Long createdByUserId;
    private String createdByNom;
    private String statut;          // DRAFT / EN_ATTENTE_VALIDATION / VALIDE / REFUSE
    private BigDecimal totalGeneral;

    public RapportLite(Long id, LocalDate jour, Long chantierId, String chantierCode, String chantierLibelle,
                       Long createdByUserId, String createdByNom, String statut, BigDecimal totalGeneral) {
        this.id = id;
        this.jour = jour;
        this.chantierId = chantierId;
        this.chantierCode = chantierCode;
        this.chantierLibelle = chantierLibelle;
        this.createdByUserId = createdByUserId;
        this.createdByNom = createdByNom;
        this.statut = statut;
        this.totalGeneral = totalGeneral;
    }

    public RapportLite() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getJour() { return jour; }
    public void setJour(LocalDate jour) { this.jour = jour; }

    public Long getChantierId() { return chantierId; }
    public void setChantierId(Long chantierId) { this.chantierId = chantierId; }

    public String getChantierCode() { return chantierCode; }
    public void setChantierCode(String chantierCode) { this.chantierCode = chantierCode; }

    public String getChantierLibelle() { return chantierLibelle; }
    public void setChantierLibelle(String chantierLibelle) { this.chantierLibelle = chantierLibelle; }

    public Long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Long createdByUserId) { this.createdByUserId = createdByUserId; }

    public String getCreatedByNom() { return createdByNom; }
    public void setCreatedByNom(String createdByNom) { this.createdByNom = createdByNom; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public BigDecimal getTotalGeneral() { return totalGeneral; }
    public void setTotalGeneral(BigDecimal totalGeneral) { this.totalGeneral = totalGeneral; }
}
