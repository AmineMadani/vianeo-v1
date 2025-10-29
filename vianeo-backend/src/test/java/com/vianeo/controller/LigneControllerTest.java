package com.vianeo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vianeo.dto.ligne.*;
import com.vianeo.model.entity.*;
import com.vianeo.model.enums.*;
import com.vianeo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
public class LigneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    private Article testArticleMaterielSsCh;
    private Fournisseur testFournisseur;

    @BeforeEach
    void setUp() {
        // Créer les entités de test
        Entite entite = new Entite("TEST", "Entité Test");
        
        testUtilisateur = new Utilisateur("Dupont", "Jean", "Développeur", "ADMIN", entite);
        utilisateurRepository.save(testUtilisateur);
        
        Chantier chantier = new Chantier("CH001", "Chantier Test", entite);
        chantierRepository.save(chantier);
        
        testRapport = new Rapport(chantier, LocalDate.now(), testUtilisateur);
        rapportRepository.save(testRapport);
        
        testArticleInterne = new Article("ART001", "Article Interne", CategorieArticle.INTERNE, TypeArticle.MATERIAUX);
        articleRepository.save(testArticleInterne);
        
        testArticleMaterielSsCh = new Article("ART002", "Matériel Sans Chauffeur", 
                CategorieArticle.EXTERNE, TypeArticle.MATERIEL_SANS_CHAUFFEUR);
        articleRepository.save(testArticleMaterielSsCh);
        
        testFournisseur = new Fournisseur("FOUR001", TypeFournisseur.INTERIM);
        fournisseurRepository.save(testFournisseur);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateLignePersonnel() throws Exception {
        LignePersonnelRequest request = new LignePersonnelRequest(
                CategoriePersonnel.OPERATIONNEL, 
                testUtilisateur.getId(),
                TypeTravail.J, 
                new BigDecimal("8.00"), 
                new BigDecimal("25.50")
        );

        mockMvc.perform(post("/api/rapports/{id}/lignes/personnel", testRapport.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categorie").value("OPERATIONNEL"))
                .andExpect(jsonPath("$.typeTravail").value("J"))
                .andExpect(jsonPath("$.quantite").value(8.00))
                .andExpect(jsonPath("$.pu").value(25.50));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateLigneInterim() throws Exception {
        LigneInterimRequest request = new LigneInterimRequest();
        request.setFournisseurId(testFournisseur.getId());
        request.setNom("Martin");
        request.setPrenom("Pierre");
        request.setTypeTravail(TypeTravail.J);
        request.setQuantite(new BigDecimal("8.00"));
        request.setPu(new BigDecimal("30.00"));

        mockMvc.perform(post("/api/rapports/{id}/lignes/interim", testRapport.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Martin"))
                .andExpect(jsonPath("$.prenom").value("Pierre"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateLigneMatInterne() throws Exception {
        LigneMatInterneRequest request = new LigneMatInterneRequest(
                testArticleInterne.getId(),
                new BigDecimal("10.00"),
                new BigDecimal("15.75")
        );

        mockMvc.perform(post("/api/rapports/{id}/lignes/matInterne", testRapport.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantite").value(10.00))
                .andExpect(jsonPath("$.pu").value(15.75));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateLigneMatInterneWithWrongCategory() throws Exception {
        // Créer un article EXTERNE pour tester la contrainte
        Article articleExterne = new Article("ART003", "Article Externe", 
                CategorieArticle.EXTERNE, TypeArticle.MATERIAUX);
        articleRepository.save(articleExterne);
        
        LigneMatInterneRequest request = new LigneMatInterneRequest(
                articleExterne.getId(),
                new BigDecimal("10.00"),
                new BigDecimal("15.75")
        );

        mockMvc.perform(post("/api/rapports/{id}/lignes/matInterne", testRapport.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateLigneLocSsCh() throws Exception {
        LigneLocationRequest request = new LigneLocationRequest(
                testArticleMaterielSsCh.getId(),
                testFournisseur.getId(),
                new BigDecimal("1.00"),
                new BigDecimal("150.00")
        );

        mockMvc.perform(post("/api/rapports/{id}/lignes/locSsCh", testRapport.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantite").value(1.00))
                .andExpect(jsonPath("$.pu").value(150.00));
    }

    @Test
    @WithMockUser(roles = "CC")
    public void testCreateLigneMateriaux() throws Exception {
        LigneMateriauxRequest request = new LigneMateriauxRequest(
                testArticleInterne.getId(),
                null, // Fournisseur optionnel
                new BigDecimal("25.500"),
                new BigDecimal("12.50")
        );

        mockMvc.perform(post("/api/rapports/{id}/lignes/materiaux", testRapport.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantite").value(25.500))
                .andExpect(jsonPath("$.pu").value(12.50));
    }

    @Test
    @WithMockUser(roles = "READONLY")
    public void testCreateLignePersonnelUnauthorized() throws Exception {
        LignePersonnelRequest request = new LignePersonnelRequest(
                CategoriePersonnel.OPERATIONNEL, 
                testUtilisateur.getId(),
                TypeTravail.J, 
                new BigDecimal("8.00"), 
                new BigDecimal("25.50")
        );

        mockMvc.perform(post("/api/rapports/{id}/lignes/personnel", testRapport.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}