package com.vianeo.repository;

import com.vianeo.model.entity.Article;
import com.vianeo.model.enums.CategorieArticle;
import com.vianeo.model.enums.TypeArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByActifTrue();

    List<Article> findByCategorieAndActifTrue(CategorieArticle categorie);

    List<Article> findByTypeAndActifTrue(TypeArticle type);

    Optional<Article> findByCode(String code);

    @Query("SELECT a FROM Article a WHERE a.categorie = :categorie AND a.type = :type AND a.actif = true")
    List<Article> findByCategorieAndTypeAndActifTrue(CategorieArticle categorie, TypeArticle type);

    @Query("SELECT a FROM Article a WHERE a.actif = true ORDER BY a.designation")
    List<Article> findAllActiveOrderByDesignation();
}
