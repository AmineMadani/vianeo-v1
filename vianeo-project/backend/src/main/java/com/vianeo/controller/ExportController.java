package com.vianeo.controller;

import com.vianeo.security.UserPrincipal;
import com.vianeo.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/rapports/excel")
    public ResponseEntity<byte[]> exportRapportsToExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(required = false) Long chantierId,
            @RequestParam(required = false) Long chefId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            // Si l'utilisateur n'est pas admin, il ne peut exporter que ses propres rapports
            if (!userPrincipal.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                chefId = userPrincipal.getId();
            }
            
            byte[] excelData = exportService.exportRapportsToExcel(dateDebut, dateFin, chantierId, chefId);
            
            String filename = String.format("rapports_%s_%s.xlsx", 
                dateDebut.toString(), dateFin.toString());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/rapports/weekly")
    public ResponseEntity<byte[]> exportWeeklyRapports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart,
            @RequestParam(required = false) Long chantierId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            LocalDate weekEnd = weekStart.plusDays(6);
            
            Long chefId = null;
            if (!userPrincipal.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                chefId = userPrincipal.getId();
            }
            
            byte[] excelData = exportService.exportWeeklyRapports(weekStart, weekEnd, chantierId, chefId);
            
            String filename = String.format("rapport_hebdomadaire_%s.xlsx", weekStart.toString());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}