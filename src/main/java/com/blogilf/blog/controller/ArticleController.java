package com.blogilf.blog.controller;

import com.blogilf.blog.model.Article;
import com.blogilf.blog.service.ArticleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping("")
    public String listArticles(Model model) {
        service.getArticles(model);
        return "article/List";
    }
    
    @GetMapping("/{slug}")
    public String getArticleBySlug(@PathVariable String slug, Model model) throws Exception {

        ResponseEntity<Void> responseEntity = service.getArticle(model, slug);
        
        switch (responseEntity.getStatusCode()) {
            case HttpStatus.FOUND:
                return "article/Detail";
            case HttpStatus.NOT_FOUND:
                return "error/404";
            default:
                throw new Exception("Smth went wrong.");
        }
    }

    @PutMapping("/{slug}")
    public String updateArticle(@PathVariable String slug,Model model, @ModelAttribute Article article) throws Exception {

        ResponseEntity<String> responseEntity = service.updateArticle(slug, article,model);
        
        switch (responseEntity.getStatusCode()) {
            case HttpStatus.NOT_FOUND:
                return "error/404";
            case HttpStatus.CONFLICT:
                return "article/Update";
            case HttpStatus.CREATED:
                return "redirect:/" + article.getSlug();
            default:
                throw new Exception("Smth went wrong");
        }
    }

    @GetMapping("/{slug}/update")
    public String updateArticleForm(@PathVariable String slug, Model model) {
        
        ResponseEntity<Void> responseEntity = service.getArticle(model, slug);

        switch (responseEntity.getStatusCode()) {
            case HttpStatus.FOUND:
                return "article/Update";
            default:
                return "error/404";
        }
    }

    @GetMapping("/create")
    public String createArticleForm(Model model) {
        service.createArticle(model);
        return "article/Create";
    }

    @PostMapping("/create")
    public String createArticle(@ModelAttribute Article article,Model model) {

        ResponseEntity<String> responseEntity = service.createArticle(article,model);

        switch (responseEntity.getStatusCode()) {
            case HttpStatus.CONFLICT:
                return "article/Create";          
            default:
                return "redirect:/" + article.getSlug();
        }
    }
}