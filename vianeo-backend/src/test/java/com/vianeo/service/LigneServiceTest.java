package com.vianeo.service;

import com.vianeo.dto.ligne.*;
import com.vianeo.model.entity.*;
import com.vianeo.model.enums.*;
import com.vianeo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
public class LigneServiceTest {

    @Autowired
    private LigneService ligneService;

    @Autowired
    private RapportRepository rapportRepository;

    @Autowired
    private ChantierRepository chantierRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    private Rapport testRapport;
    private Utilisateur testUtilisateur;
    private Article testArticleInterne;
    private Article testArticleExterne;
    private Article testArticleMaterielSsCh;
    private Fournisseur testFournisseur;

    @BeforeEach
    void setUp() {
        Entite entite = new Entite("TEST", "Entité Test");
        
        testUtilisateur = new Utilisateur("Dupont", "Jean", "Développeur", "ADMIN", entite);
        utilisateurRepository.save(testUtilisateur);
        
        Chantier chantier = new Chantier("CH001", "Chantier Test", entite);
        chantierRepository.save(chantier);
        
        testRapport = new Rapport(chantier, LocalDate.now(), testUtilisateur);
        rapportRepository.save(testRapport);
        
        testArticleInterne = new Article("ART001", "Article Interne", CategorieArticle.INTERNE, TypeArticle.MATERIAUX);
        articleRepository.save(testArticleInterne);
        
        testArticleExterne = new Article("ART002", "Article Externe", CategorieArticle.EXTERNE, TypeArticle.MATERIAUX);
        articleRepository.save(testArticleExterne);
        
        testArticleMaterielSsCh = new Article("ART003", "Matériel Sans Chauffeur", 
                CategorieArticle.EXTERNE, TypeArticle.MATERIEL_SANS_CHAUFFEUR);
        articleRepository.save(testArticleMaterielSsCh);
        
        testFournisseur = new Fournisseur("FOUR001", TypeFournisseur.INTERIM);
        fournisseurRepository.save(testFournisseur);
    }

    @Test
    public void testCreateLignePersonnel() {
        LignePersonnelRequest request = new LignePersonnelRequest(
                CategoriePersonnel.OPERATIONNEL, 
                testUtilisateur.getId(),
                TypeTravail.J, 
                new BigDecimal("8.00"), 
                new BigDecimal("25.50")
        );

        LignePersonnel ligne = ligneService.createLignePersonnel(testRapport.getId(), request);

        assertNotNull(ligne.getId());
        assertEquals(CategoriePersonnel.OPERATIONNEL, ligne.getCategorie());
        assertEquals(TypeTravail.J, ligne.getTypeTravail());
        assertEquals(new BigDecimal("8.00"), ligne.getQuantite());
        assertEquals(new BigDecimal("25.50"), ligne.getPu());
        assertEquals(new BigDecimal("204.00"), ligne.getTotal());
    }

    @Test
    public void testCreateLigneMatInterneWithValidCategory() {
        LigneMatInterneRequest request = new LigneMatInterneRequest(
                testArticleInterne.getId(),
                new BigDecimal("10.00"),
                new BigDecimal("15.75")
        );

        LigneMatInterne ligne = ligneService.createLigneMatInterne(testRapport.getId(), request);

        assertNotNull(ligne.getId());
        assertEquals(testArticleInterne.getId(), ligne.getArticle().getId());
        assertEquals(new BigDecimal("10.00"), ligne.getQuantite());
        assertEquals(new BigDecimal("15.75"), ligne.getPu());
    }

    @Test
    public void testCreateLigneMatInterneWithInvalidCategory() {
        LigneMatInterneRequest request = new LigneMatInterneRequest(
                testArticleExterne.getId(), // Article EXTERNE
                new BigDecimal("10.00"),
                new BigDecimal("15.75")
        );

        assertThrows(IllegalArgumentException.class, () -> {
            ligneService.createLigneMatInterne(testRapport.getId(), request);
        });
    }

    @Test
    public void testCreateLigneLocSsChWithValidType() {
        LigneLocationRequest request = new LigneLocationRequest(
                testArticleMaterielSsCh.getId(),
                testFournisseur.getId(),
                new BigDecimal("1.00"),
                new BigDecimal("150.00")
        );

        LigneLocSsCh ligne = ligneService.createLigneLocSsCh(testRapport.getId(), request);

        assertNotNull(ligne.getId());
        assertEquals(testArticleMaterielSsCh.getId(), ligne.getArticle().getId());
        assertEquals(new BigDecimal("1.00"), ligne.getQuantite());
        assertEquals(new BigDecimal("150.00"), ligne.getPu());
    }

    @Test
    public void testCreateLigneLocSsChWithInvalidType() {
        LigneLocationRequest request = new LigneLocationRequest(
                testArticleInterne.getId(), // Article MATERIAUX au lieu de MATERIEL_SANS_CHAUFFEUR
                testFournisseur.getId(),
                new BigDecimal("1.00"),
                new BigDecimal("150.00")
        );

        assertThrows(IllegalArgumentException.class, () -> {
            ligneService.createLigneLocSsCh(testRapport.getId(), request);
        });
    }

    @Test
    public void testCreateLigneMateriauxWithoutFournisseur() {
        LigneMateriauxRequest request = new LigneMateriauxRequest(
                testArticleInterne.getId(),
                null, // Fournisseur optionnel
                new BigDecimal("25.500"),
                new BigDecimal("12.50")
        );

        LigneMateriaux ligne = ligneService.createLigneMateriaux(testRapport.getId(), request);

        assertNotNull(ligne.getId());
        assertEquals(testArticleInterne.getId(), ligne.getArticle().getId());
        assertNull(ligne.getFournisseur());
        assertEquals(new BigDecimal("25.500"), ligne.getQuantite());
        assertEquals(new BigDecimal("12.50"), ligne.getPu());
    }

    @Test
    public void testCreateLigneMateriauxWithFournisseur() {
        LigneMateriauxRequest request = new LigneMateriauxRequest(
                testArticleInterne.getId(),
                testFournisseur.getId(),
                new BigDecimal("25.500"),
                new BigDecimal("12.50")
        );

        LigneMateriaux ligne = ligneService.createLigneMateriaux(testRapport.getId(), request);

        assertNotNull(ligne.getId());
        assertEquals(testArticleInterne.getId(), ligne.getArticle().getId());
        assertEquals(testFournisseur.getId(), ligne.getFournisseur().getId());
        assertEquals(new BigDecimal("25.500"), ligne.getQuantite());
        assertEquals(new BigDecimal("12.50"), ligne.getPu());
    }
}