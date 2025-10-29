package com.vianeo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyRapportSummary(
        LocalDate jour,
        Long rapportId,
        String statut,
        BigDecimal total
) {}