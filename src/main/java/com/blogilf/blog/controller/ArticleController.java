package com.blogilf.blog.controller;

import com.blogilf.blog.dto.ArticleDTO;
import com.blogilf.blog.model.Article;
import com.blogilf.blog.service.ArticleService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    public List<ArticleDTO> getArticles() {
        return articleService.getArticles();
    }
    
    @GetMapping("/{slug}")
    public Article getArticleBySlug(@PathVariable String slug) {
        return articleService.getArticle(slug);
    }

    @PutMapping("/{slug}")
    public Article updateArticle(@PathVariable String slug, @RequestBody Article article) {
        return articleService.updateArticle(slug, article);
    }

    @DeleteMapping("/{slug}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable String slug) {
        articleService.deleteArticle(slug);
    }

    @PostMapping("/create")
    public Article createArticle(@Valid @RequestBody Article article) {
        return articleService.createArticle(article);
    }
}