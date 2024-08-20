package com.blogilf.blog.repository;

import com.blogilf.blog.model.Article;
import com.blogilf.blog.projection.ArticleProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
// consider extending JpaSpecificationExecutor<Article>
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findBySlug(String slug);
    boolean existsBySlug(String slug);
    Page<Article> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    List<Article> findAll(Specification<Article> spec);
    List<ArticleProjection> findAllProjectedBy();
}