package com.blogilf.blog.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.blogilf.blog.exception.CustomBadRequestException;
import com.blogilf.blog.exception.CustomForbiddenException;
import com.blogilf.blog.exception.CustomResourceNotFoundException;
import com.blogilf.blog.model.entity.Article;
import com.blogilf.blog.model.entity.User;
import com.blogilf.blog.model.projection.ArticleProjection;
import com.blogilf.blog.model.repository.ArticleRepository;
import com.blogilf.blog.model.repository.UserRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.List;

@Service
public class ArticleService {
    
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final Validator validator;

    ArticleService(ArticleRepository articleRepository,UserRepository userRepository,Validator validator){
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.validator = validator;
    }

    public List<ArticleProjection> getArticles(){
        // return articleRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        return articleRepository.findAllProjectedBy();
    }

    public Article createArticle(Article article) {
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByUsername(username).orElseThrow(() -> new CustomResourceNotFoundException("User not found."));
        
        article.setAuthor(author);

        if (articleRepository.existsBySlug(article.getSlug())){
            throw new CustomBadRequestException("Slug already taken.");
        }
        
        return articleRepository.save(article);
    }

    public Article getArticle(String slug) {

        Optional<Article> article = articleRepository.findBySlug(slug);
        
        if (article.isPresent()) {
            return article.get();            
        }

        throw new CustomResourceNotFoundException("Article not found.");
    }

    public Article updateArticle(String slug,Article article) {
        
        Optional<Article> updatedArticleOptional = articleRepository.findBySlug(slug);
        
        // check for correct slug
        if (updatedArticleOptional.isEmpty()){
            throw new CustomResourceNotFoundException("Article not found.");
        }

        // check for correct author
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals(updatedArticleOptional.get().getAuthor().getUsername())) {
            throw new CustomForbiddenException("User is not the author.");
        }

        // validate the article obj submitted by the request
        Set<ConstraintViolation<Article>> violations = validator.validate(article);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        // check for duplicate slug
        if (!slug.equals(article.getSlug()) && articleRepository.existsBySlug(article.getSlug())){
            throw new CustomBadRequestException("Slug already taken.");
        }

        Article updatedArticle = updatedArticleOptional.get();

        updatedArticle.setSlug(article.getSlug());
        updatedArticle.setTitle(article.getTitle());
        updatedArticle.setContent(article.getContent());
        
        return articleRepository.save(updatedArticle);
    }

    // private ArticleDTO convertToDto(Article article) {
        
    //     ArticleDTO dto = new ArticleDTO();
    //     dto.setTitle(article.getTitle());
    //     dto.setSlug(article.getSlug());
    //     dto.setContent(article.getContent());
    //     dto.setPublishedDate(article.getPublishedDate());

    //     User user = article.getAuthor();
    //     dto.setAuthorName(user.getName());
    //     dto.setAuthorUsername(user.getUsername());

    //     return dto;
    // }

    public void deleteArticle(String slug) {
        
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new CustomResourceNotFoundException("Article not found."));
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals(article.getAuthor().getUsername())) {
            throw new CustomForbiddenException("User is not the author.");
        }

        articleRepository.delete(article);
    }

}
