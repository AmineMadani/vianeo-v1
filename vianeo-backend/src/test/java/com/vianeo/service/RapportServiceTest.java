package com.vianeo.service;

import com.vianeo.model.entity.*;
import com.vianeo.model.enums.StatutRapport;
import com.vianeo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
public class RapportServiceTest {

    @Autowired
    private RapportService rapportService;

    @Autowired
    private RapportRepository rapportRepository;

    @Autowired
    private ChantierRepository chantierRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private Rapport testRapport;

    @BeforeEach
    void setUp() {
        Entite entite = new Entite("TEST", "Entité Test");
        
        Utilisateur utilisateur = new Utilisateur("Dupont", "Jean", "Développeur", "ADMIN", entite);
        utilisateurRepository.save(utilisateur);
        
        Chantier chantier = new Chantier("CH001", "Chantier Test", entite);
        chantierRepository.save(chantier);
        
        testRapport = new Rapport(chantier, LocalDate.now(), utilisateur);
        rapportRepository.save(testRapport);
    }

    @Test
    public void testSoumettre() {
        assertEquals(StatutRapport.DRAFT, testRapport.getStatut());
        
        Rapport rapportSoumis = rapportService.soumettre(testRapport.getId());
        
        assertEquals(StatutRapport.EN_ATTENTE_VALIDATION, rapportSoumis.getStatut());
    }

    @Test
    public void testValider() {
        testRapport.setStatut(StatutRapport.EN_ATTENTE_VALIDATION);
        rapportRepository.save(testRapport);
        
        Rapport rapportValide = rapportService.valider(testRapport.getId());
        
        assertEquals(StatutRapport.VALIDE, rapportValide.getStatut());
    }

    @Test
    public void testRefuser() {
        testRapport.setStatut(StatutRapport.EN_ATTENTE_VALIDATION);
        rapportRepository.save(testRapport);
        
        String motif = "Données incomplètes";
        Rapport rapportRefuse = rapportService.refuser(testRapport.getId(), motif);
        
        assertEquals(StatutRapport.REFUSE, rapportRefuse.getStatut());
        assertEquals(motif, rapportRefuse.getCommentaireCdt());
    }

    @Test
    public void testSoumettreRapportDejaValide() {
        testRapport.setStatut(StatutRapport.VALIDE);
        rapportRepository.save(testRapport);
        
        assertThrows(IllegalStateException.class, () -> {
            rapportService.soumettre(testRapport.getId());
        });
    }

    @Test
    public void testValiderRapportDraft() {
        assertEquals(StatutRapport.DRAFT, testRapport.getStatut());
        
        assertThrows(IllegalStateException.class, () -> {
            rapportService.valider(testRapport.getId());
        });
    }
}