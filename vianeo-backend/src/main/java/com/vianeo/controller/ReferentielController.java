// com.vianeo.controller.ReferentielController
package com.vianeo.controller;

import com.vianeo.dto.FournisseurDto;
import com.vianeo.mapper.FournisseurMapper;
import com.vianeo.model.entity.Article;
import com.vianeo.model.entity.Fournisseur;
import com.vianeo.model.enums.CategorieArticle;
import com.vianeo.model.enums.TypeArticle;
import com.vianeo.model.enums.TypeFournisseur;
import com.vianeo.service.ArticleService;
import com.vianeo.service.FournisseurService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ref")
@PreAuthorize("hasRole('ADMIN') or hasRole('CDT') or hasRole('CC') or hasRole('READONLY')")
public class ReferentielController {

    private final ArticleService articleService;
    private final FournisseurService fournisseurService;

    public ReferentielController(ArticleService articleService,
                                 FournisseurService fournisseurService) {
        this.articleService = articleService;
        this.fournisseurService = fournisseurService;
    }

    /* ===== Articles ===== */
/*
    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getArticles(
            @RequestParam(required = false) CategorieArticle cat,
            @RequestParam(required = false) String search) {

        List<Article> articles;
        if (cat != null) {
            articles = articleService.findByCategorie(cat);
        } else if (search != null && !search.trim().isEmpty()) {
            articles = articleService.searchByCodeOrDesignation(search);
        } else {
            articles = articleService.findAllActifs();
        }
        return ResponseEntity.ok(articles);
    } */

    // GET /api/referentiel/articles?cat=...&type=...&search=...
    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getArticles(
            @RequestParam(required = false) CategorieArticle cat,
            @RequestParam(required = false) TypeArticle type,
            @RequestParam(required = false) String search) {
        List<Article> articles;
        if (cat != null && type != null) {
            articles = articleService.findByCategorieAndType(cat, type);
        } else if (cat != null) {
            articles = articleService.findByCategorie(cat);
        } else if (search != null && !search.isBlank()) {
            articles = articleService.searchByCodeOrDesignation(search);
        } else {
            articles = articleService.findAllActifs();
        }
        return ResponseEntity.ok(articles);
    }


    @GetMapping("/articles/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.findById(id));
    }


    /* ===== Fournisseurs ===== */

    @GetMapping("/fournisseurs")
    public ResponseEntity<List<FournisseurDto>> getFournisseurs(
            @RequestParam(required = false) TypeFournisseur type,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean actif) {

        List<Fournisseur> list = fournisseurService.findByFilters(type, search, actif);
        return ResponseEntity.ok(list.stream().map(FournisseurMapper::toDto).toList());
    }

    @GetMapping("/fournisseurs/{id}")
    public ResponseEntity<FournisseurDto> getFournisseur(@PathVariable Long id) {
        var entity = fournisseurService.findById(id);
        return ResponseEntity.ok(FournisseurMapper.toDto(entity));
    }

    @PostMapping("/fournisseurs")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CDT')")
    public ResponseEntity<FournisseurDto> createFournisseur(@RequestBody FournisseurDto dto) {
        var entity = FournisseurMapper.toEntity(dto);
        entity.setId(null); // sécurité
        var saved = fournisseurService.create(entity);
        return ResponseEntity
                .created(URI.create("/api/ref/fournisseurs/" + saved.getId()))
                .body(FournisseurMapper.toDto(saved));
    }

    @PutMapping("/fournisseurs/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CDT')")
    public ResponseEntity<FournisseurDto> updateFournisseur(@PathVariable Long id,
                                                            @RequestBody FournisseurDto dto) {
        var updated = fournisseurService.update(id, FournisseurMapper.toEntity(dto));
        return ResponseEntity.ok(FournisseurMapper.toDto(updated));
    }

    @DeleteMapping("/fournisseurs/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CDT')")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable Long id) {
        fournisseurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** Gardé pour compat front éventuel */
    @GetMapping("/fournisseurs/active")
    public ResponseEntity<List<FournisseurDto>> getActiveFournisseurs(
            @RequestParam(required = false) TypeFournisseur type) {
        var list = (type != null)
                ? fournisseurService.findActiveByType(type)
                : fournisseurService.findAllActifs();
        return ResponseEntity.ok(list.stream().map(FournisseurMapper::toDto).toList());
    }
}
