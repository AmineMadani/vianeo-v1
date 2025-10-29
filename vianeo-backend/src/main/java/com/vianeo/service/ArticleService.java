package com.vianeo.service;

import com.vianeo.model.entity.Article;
import com.vianeo.model.enums.CategorieArticle;
import com.vianeo.model.enums.TypeArticle;
import com.vianeo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    
    @Autowired
    private ArticleRepository articleRepository;


    public List<Article> getArticlesByType(TypeArticle type) {
        return articleRepository.findByTypeAndActifTrue(type);
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return articleRepository.existsById(id);
    }

    // Correspond à articleService.findAllActifs()
    public List<Article> findAllActifs() {
        return articleRepository.findAllActiveOrderByDesignation();
    }

    // Correspond à articleService.findByCategorie(cat)
    public List<Article> findByCategorie(CategorieArticle categorie) {
        return articleRepository.findByCategorieAndActifTrue(categorie);
    }

    public List<Article> findByCategorieAndType(CategorieArticle cat, TypeArticle type) {
        return articleRepository.findByCategorieAndTypeAndActifTrue(cat, type);
    }


    // Correspond à articleService.searchByCodeOrDesignation(search)
    public List<Article> searchByCodeOrDesignation(String q) {
        if (q == null || q.isBlank()) {
            return findAllActifs();
        }
        // tu peux ajouter une query custom dans le repository si besoin
        return articleRepository.findAllActiveOrderByDesignation().stream()
                .filter(a -> a.getCode().toLowerCase().contains(q.toLowerCase())
                        || a.getDesignation().toLowerCase().contains(q.toLowerCase()))
                .toList();
    }

    // Correspond à articleService.findById(id)
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article introuvable: id=" + id));
    }

    // helpers dans un service "LigneService" (ou dans chaque service dédié)
    private void assertCategorieInterne(Article a) {
        if (a.getCategorie() != CategorieArticle.INTERNE) {
            throw new IllegalArgumentException("Article doit être de catégorie INTERNE");
        }
    }

    private void assertType(Article a, TypeArticle expected) {
        if (a.getType() != expected) {
            throw new IllegalArgumentException("Article de type " + a.getType() + " incompatible, attendu: " + expected);
        }
    }


}
