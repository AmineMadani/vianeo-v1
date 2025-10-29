package com.vianeo.dto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
public record CreateRapportRequest(Long chantierId, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate jour, Boolean prefill) {}