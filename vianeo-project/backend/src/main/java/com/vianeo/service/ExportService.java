package com.vianeo.service;

import com.vianeo.entity.MaterielRapport;
import com.vianeo.entity.PersonnelRapport;
import com.vianeo.entity.RapportChantier;
import com.vianeo.repository.RapportChantierRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {

    @Autowired
    private RapportChantierRepository rapportRepository;

    public byte[] exportRapportsToExcel(LocalDate dateDebut, LocalDate dateFin, Long chantierId, Long chefId) throws IOException {
        List<RapportChantier> rapports = getRapports(dateDebut, dateFin, chantierId, chefId);
        
        try (Workbook workbook = new XSSFWorkbook()) {
            createRapportsSheet(workbook, rapports);
            createPersonnelSheet(workbook, rapports);
            createMaterielSheet(workbook, rapports);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] exportWeeklyRapports(LocalDate weekStart, LocalDate weekEnd, Long chantierId, Long chefId) throws IOException {
        List<RapportChantier> rapports = getRapports(weekStart, weekEnd, chantierId, chefId);
        
        try (Workbook workbook = new XSSFWorkbook()) {
            createWeeklyRapportSheet(workbook, rapports, weekStart);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private List<RapportChantier> getRapports(LocalDate dateDebut, LocalDate dateFin, Long chantierId, Long chefId) {
        if (chefId != null) {
            return rapportRepository.findByChefChantierAndDateBetween(
                null, // Nous devrons récupérer l'utilisateur par son ID
                dateDebut, dateFin
            );
        } else {
            return rapportRepository.findByDateBetween(dateDebut, dateFin);
        }
    }

    private void createRapportsSheet(Workbook workbook, List<RapportChantier> rapports) {
        Sheet sheet = workbook.createSheet("Rapports");
        
        // Créer le style pour les en-têtes
        CellStyle headerStyle = createHeaderStyle(workbook);
        
        // Créer les en-têtes
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Date", "Chantier", "Chef de Chantier", "Statut", "Observations", "Problèmes", "Sécurité", "Météo"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Remplir les données
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int rowNum = 1;
        
        for (RapportChantier rapport : rapports) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(rapport.getDate().format(dateFormatter));
            row.createCell(1).setCellValue(rapport.getChantier().getNom());
            row.createCell(2).setCellValue(rapport.getChefChantier().getNom() + " " + rapport.getChefChantier().getPrenom());
            row.createCell(3).setCellValue(rapport.getStatut().toString());
            row.createCell(4).setCellValue(rapport.getObservations() != null ? rapport.getObservations() : "");
            row.createCell(5).setCellValue(rapport.getProblemes() != null ? rapport.getProblemes() : "");
            row.createCell(6).setCellValue(rapport.getSecurite() != null ? rapport.getSecurite() : "");
            row.createCell(7).setCellValue(rapport.getMeteo() != null ? rapport.getMeteo() : "");
        }
        
        // Ajuster la largeur des colonnes
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createPersonnelSheet(Workbook workbook, List<RapportChantier> rapports) {
        Sheet sheet = workbook.createSheet("Personnel");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Date", "Chantier", "Nom", "Prénom", "Type", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Total Heures", "Taux Journalier", "Coût Total", "Fournisseur"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int rowNum = 1;
        
        for (RapportChantier rapport : rapports) {
            for (PersonnelRapport personnel : rapport.getPersonnel()) {
                Row row = sheet.createRow(rowNum++);
                
                BigDecimal totalHeures = personnel.getHeuresLundi()
                    .add(personnel.getHeuresMardi())
                    .add(personnel.getHeuresMercredi())
                    .add(personnel.getHeuresJeudi())
                    .add(personnel.getHeuresVendredi())
                    .add(personnel.getHeuresSamedi());
                
                BigDecimal coutTotal = totalHeures.multiply(personnel.getTauxJournalier());
                
                row.createCell(0).setCellValue(rapport.getDate().format(dateFormatter));
                row.createCell(1).setCellValue(rapport.getChantier().getNom());
                row.createCell(2).setCellValue(personnel.getNom());
                row.createCell(3).setCellValue(personnel.getPrenom());
                row.createCell(4).setCellValue(personnel.getType().toString());
                row.createCell(5).setCellValue(personnel.getHeuresLundi().doubleValue());
                row.createCell(6).setCellValue(personnel.getHeuresMardi().doubleValue());
                row.createCell(7).setCellValue(personnel.getHeuresMercredi().doubleValue());
                row.createCell(8).setCellValue(personnel.getHeuresJeudi().doubleValue());
                row.createCell(9).setCellValue(personnel.getHeuresVendredi().doubleValue());
                row.createCell(10).setCellValue(personnel.getHeuresSamedi().doubleValue());
                row.createCell(11).setCellValue(totalHeures.doubleValue());
                row.createCell(12).setCellValue(personnel.getTauxJournalier().doubleValue());
                row.createCell(13).setCellValue(coutTotal.doubleValue());
                row.createCell(14).setCellValue(personnel.getFournisseur() != null ? personnel.getFournisseur() : "");
            }
        }
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createMaterielSheet(Workbook workbook, List<RapportChantier> rapports) {
        Sheet sheet = workbook.createSheet("Matériel");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Date", "Chantier", "Désignation", "Type", "Quantité", "Unité", "Prix Unitaire", "Coût Total", "Fournisseur", "Avec Chauffeur"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int rowNum = 1;
        
        for (RapportChantier rapport : rapports) {
            for (MaterielRapport materiel : rapport.getMateriel()) {
                Row row = sheet.createRow(rowNum++);
                
                BigDecimal coutTotal = materiel.getQuantite().multiply(materiel.getPrixUnitaire());
                
                row.createCell(0).setCellValue(rapport.getDate().format(dateFormatter));
                row.createCell(1).setCellValue(rapport.getChantier().getNom());
                row.createCell(2).setCellValue(materiel.getDesignation());
                row.createCell(3).setCellValue(materiel.getType().toString());
                row.createCell(4).setCellValue(materiel.getQuantite().doubleValue());
                row.createCell(5).setCellValue(materiel.getUnite());
                row.createCell(6).setCellValue(materiel.getPrixUnitaire().doubleValue());
                row.createCell(7).setCellValue(coutTotal.doubleValue());
                row.createCell(8).setCellValue(materiel.getFournisseur() != null ? materiel.getFournisseur() : "");
                row.createCell(9).setCellValue(materiel.isAvecChauffeur() ? "Oui" : "Non");
            }
        }
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createWeeklyRapportSheet(Workbook workbook, List<RapportChantier> rapports, LocalDate weekStart) {
        Sheet sheet = workbook.createSheet("Rapport Hebdomadaire");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle titleStyle = createTitleStyle(workbook);
        
        // Titre
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("RAPPORT HEBDOMADAIRE - Semaine du " + weekStart.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        titleCell.setCellStyle(titleStyle);
        
        // Résumé par chantier
        int currentRow = 2;
        
        // Grouper les rapports par chantier
        rapports.stream()
            .collect(java.util.stream.Collectors.groupingBy(r -> r.getChantier().getNom()))
            .forEach((chantierNom, rapportsChantier) -> {
                // En-tête du chantier
                Row chantierRow = sheet.createRow(currentRow);
                Cell chantierCell = chantierRow.createCell(0);
                chantierCell.setCellValue("CHANTIER: " + chantierNom);
                chantierCell.setCellStyle(headerStyle);
                
                // Détails des rapports pour ce chantier
                // ... (logique pour afficher les détails)
            });
        
        sheet.autoSizeColumn(0);
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        return style;
    }
}