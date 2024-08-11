package com.blogilf.blog.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.blogilf.blog.model.Article;
import com.blogilf.blog.repository.ArticleRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ArticleService {
    
    private final ArticleRepository repo;

    ArticleService(ArticleRepository repo){
        this.repo = repo;
    }

    public ResponseEntity<String> createArticle(Article article,Model model) {

        if (repo.existsBySlug(article.getSlug())){
            model.addAttribute("error", "Slug is already chosen.");
            model.addAttribute("article", article);
            return new ResponseEntity<>("Slug is already used.", HttpStatus.CONFLICT);
        }
        
        repo.save(article);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public void createArticle(Model model){
        model.addAttribute("article", new Article());
    }

    public void getArticles(Model model){
        model.addAttribute("articles", repo.findAll());
    }

    public ResponseEntity<Void> getArticle(Model model,String slug){
        Optional<Article> article = repo.findBySlug(slug);
        if (article.isPresent()) {
            model.addAttribute("article", article.get());
            return new ResponseEntity<>(HttpStatus.FOUND);            
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> updateArticle(String slug,Article article,Model model){
        
        Optional<Article> updatedArticleOptional = repo.findBySlug(slug);     
        
        if (updatedArticleOptional.isEmpty()){
            return new ResponseEntity<>("There is no article to update.",HttpStatus.NOT_FOUND);
        }

        if (!slug.equals(article.getSlug()) && repo.existsBySlug(article.getSlug())){

            // set slug to the original one!
            article.setSlug(slug);

            model.addAttribute("error", "Slug is already used.");
            model.addAttribute("article", article);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        // ready to update

        Article updatedArticle = updatedArticleOptional.get();

        updatedArticle.setSlug(article.getSlug());
        updatedArticle.setTitle(article.getTitle());
        updatedArticle.setContent(article.getContent());

        repo.save(updatedArticle);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    // init data
    @PostConstruct
    public void init(){
        repo.save(new Article(null,"First article","111","11111111111111111",null));
        repo.save(new Article(null,"Second article!","222","22222222222222222222",null));
        repo.save(new Article(null,"Third article!","333","333333333333333333",null));
        repo.save(new Article(null,"Forth article!","444","4444444444444444444444444",null));
    
        System.out.println("\nADDING ITEMS\n");
    }

}
