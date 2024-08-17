package com.blogilf.blog.controller;

import com.blogilf.blog.dto.ArticleDTO;
import com.blogilf.blog.model.Article;
import com.blogilf.blog.service.ArticleService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<ArticleDTO> getArticles() {
        return service.getArticles();
    }
    
    @GetMapping("/{slug}")
    public Article getArticleBySlug(@PathVariable String slug) {
        return service.getArticle(slug);
    }

    @PutMapping("/{slug}")
    public Article updateArticle(@PathVariable String slug, @RequestBody Article article) {
        return service.updateArticle(slug, article);
    }

    @PostMapping("/create")
    public Article createArticle(@Valid @RequestBody Article article) {
        return service.createArticle(article);
    }
}