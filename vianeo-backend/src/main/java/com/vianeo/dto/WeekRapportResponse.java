package com.vianeo.dto;

import com.vianeo.dto.DailyRapportSummary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public record WeekRapportResponse(
        Long chantierId,
        LocalDate weekStart,
        List<DailyRapportSummary> days
) {}
