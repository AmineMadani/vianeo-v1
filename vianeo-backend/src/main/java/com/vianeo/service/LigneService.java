package com.vianeo.service;

import com.vianeo.dto.ligne.*;
import com.vianeo.model.entity.*;
import com.vianeo.model.enums.CategorieArticle;
import com.vianeo.model.enums.TypeArticle;
import com.vianeo.repository.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LigneService {

    @Autowired private RapportRepository rapportRepository;
    @Autowired private UtilisateurRepository utilisateurRepository;
    @Autowired private ArticleRepository articleRepository;
    @Autowired private FournisseurRepository fournisseurRepository;

    @Autowired private LignePersonnelRepository lignePersonnelRepository;
    @Autowired private LigneInterimRepository ligneInterimRepository;
    @Autowired private LigneMatInterneRepository ligneMatInterneRepository;
    @Autowired private LigneLocSsChRepository ligneLocSsChRepository;
    @Autowired private LigneLocAvecChRepository ligneLocAvecChRepository;
    @Autowired private LigneTransportRepository ligneTransportRepository;
    @Autowired private LignePrestaExtRepository lignePrestaExtRepository;
    @Autowired private LigneMateriauxRepository ligneMateriauxRepository;

    @Autowired private EntityManager em;

    /* ===================== Helpers ===================== */

    private Rapport refRapport(Long id) {
        // proxy (pas de SELECT si pas besoin immédiat des champs)
        return rapportRepository.getReferenceById(id);
    }

    private Article refArticle(Long id) {
        return articleRepository.getReferenceById(id);
    }

    private Fournisseur requireFournisseur(Long id) {
        if (id == null) throw new IllegalArgumentException("Fournisseur obligatoire");
        // proxy + existence garantie par la FK lors du flush
        return fournisseurRepository.getReferenceById(id);
    }

    private void assertCatInterne(Article a) {
        if (a.getCategorie() != CategorieArticle.INTERNE) {
            throw new IllegalArgumentException("L'article doit être de catégorie INTERNE");
        }
    }

    private void assertType(Article a, TypeArticle expected) {
        if (a.getType() != expected) {
            throw new IllegalArgumentException(
                    "Type d'article invalide: " + a.getType() + " (attendu: " + expected + ")");
        }
    }

    /** Sauve + hydrate les colonnes générées en base (ex. total). */
    private <T> T saveAndRefresh(T entity) {
        // Le repository spécifique a déjà fait le save, ici on force la synchro & le refresh
        em.flush();
        em.refresh(entity);
        return entity;
    }

    /* ===================== Personnel / Intérim ===================== */

    public LignePersonnel createLignePersonnel(Long rapportId, LignePersonnelRequest req) {
        Rapport rapport = refRapport(rapportId);
        Utilisateur utilisateur = utilisateurRepository.getReferenceById(req.getUtilisateurId());

        LignePersonnel ligne = new LignePersonnel(
                rapport,
                req.getCategorie(),
                utilisateur,
                req.getTypeTravail(),
                req.getQuantite(),
                req.getPu()
        );

        ligne = lignePersonnelRepository.save(ligne);
        // (pas de colonne calculée ici en général, refresh inoffensif)
        return saveAndRefresh(ligne);
    }

    public LigneInterim createLigneInterim(Long rapportId, LigneInterimRequest req) {
        Rapport rapport = refRapport(rapportId);
        Fournisseur fournisseur = requireFournisseur(req.getFournisseurId());

        LigneInterim ligne = new LigneInterim(
                rapport,
                fournisseur,
                req.getNom(),
                req.getPrenom(),
                req.getTypeTravail(),
                req.getQuantite(),
                req.getPu()
        );

        ligne = ligneInterimRepository.save(ligne);
        return saveAndRefresh(ligne);
    }

    /* ===================== Matériel interne ===================== */

    public LigneMatInterne createLigneMatInterne(Long rapportId, LigneMatInterneRequest req) {
        Rapport rapport = refRapport(rapportId);
        Article article = refArticle(req.getArticleId());
        assertCatInterne(article);

        LigneMatInterne ligne = new LigneMatInterne(
                rapport,
                article,
                req.getQuantite(),
                req.getPu()
        );

        ligne = ligneMatInterneRepository.save(ligne);
        return saveAndRefresh(ligne); // hydrate total
    }

    /* ===================== Locations / Transport / Presta ===================== */

    public LigneLocSsCh createLigneLocSsCh(Long rapportId, LigneLocationRequest req) {
        Rapport rapport = refRapport(rapportId);
        Article article = refArticle(req.getArticleId());
        assertType(article, TypeArticle.MATERIEL_SANS_CHAUFFEUR);
        Fournisseur fournisseur = requireFournisseur(req.getFournisseurId());

        LigneLocSsCh ligne = new LigneLocSsCh(
                rapport,
                article,
                fournisseur,
                req.getQuantite(),
                req.getPu()
        );

        ligne = ligneLocSsChRepository.save(ligne);
        return saveAndRefresh(ligne);
    }

    public LigneLocAvecCh createLigneLocAvecCh(Long rapportId, LigneLocationRequest req) {
        Rapport rapport = refRapport(rapportId);
        Article article = refArticle(req.getArticleId());
        assertType(article, TypeArticle.MATERIEL_AVEC_CHAUFFEUR);
        Fournisseur fournisseur = requireFournisseur(req.getFournisseurId());

        LigneLocAvecCh ligne = new LigneLocAvecCh(
                rapport,
                article,
                fournisseur,
                req.getQuantite(),
                req.getPu()
        );

        ligne = ligneLocAvecChRepository.save(ligne);
        return saveAndRefresh(ligne);
    }

    public LigneTransport createLigneTransport(Long rapportId, LigneLocationRequest req) {
        Rapport rapport = refRapport(rapportId);
        Article article = refArticle(req.getArticleId());
        assertType(article, TypeArticle.TRANSPORT);
        Fournisseur fournisseur = requireFournisseur(req.getFournisseurId());

        LigneTransport ligne = new LigneTransport(
                rapport,
                article,
                fournisseur,
                req.getQuantite(),
                req.getPu()
        );

        ligne = ligneTransportRepository.save(ligne);
        return saveAndRefresh(ligne);
    }

    public LignePrestaExt createLignePrestaExt(Long rapportId, LigneLocationRequest req) {
        Rapport rapport = refRapport(rapportId);
        Article article = refArticle(req.getArticleId());
        assertType(article, TypeArticle.PRESTATION_EXT);
        Fournisseur fournisseur = requireFournisseur(req.getFournisseurId());

        LignePrestaExt ligne = new LignePrestaExt(
                rapport,
                article,
                fournisseur,
                req.getQuantite(),
                req.getPu()
        );

        ligne = lignePrestaExtRepository.save(ligne);
        return saveAndRefresh(ligne);
    }

    /* ===================== Matériaux ===================== */

    public LigneMateriaux createLigneMateriaux(Long rapportId, LigneMateriauxRequest req) {
        Rapport rapport = refRapport(rapportId);
        Article article = refArticle(req.getArticleId());
        assertType(article, TypeArticle.MATERIAUX);

        Fournisseur fournisseur = (req.getFournisseurId() != null)
                ? fournisseurRepository.getReferenceById(req.getFournisseurId())
                : null; // optionnel

        LigneMateriaux ligne = new LigneMateriaux(
                rapport,
                article,
                fournisseur,
                req.getQuantite(),
                req.getPu()
        );

        ligne = ligneMateriauxRepository.save(ligne);
        return saveAndRefresh(ligne); // hydrate total
    }
}
