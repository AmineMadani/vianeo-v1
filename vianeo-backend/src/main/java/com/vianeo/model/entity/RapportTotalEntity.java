package com.vianeo.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "v_rapport_totaux", schema = "vianeo")
public class RapportTotalEntity {

    @Id
    @Column(name = "rapport_id")
    private Long rapportId;

    @Column(name = "total_general")
    private BigDecimal totalGeneral;

    // getters/setters
    public Long getRapportId() { return rapportId; }
    public void setRapportId(Long rapportId) { this.rapportId = rapportId; }

    public BigDecimal getTotalGeneral() { return totalGeneral; }
    public void setTotalGeneral(BigDecimal totalGeneral) { this.totalGeneral = totalGeneral; }
}
