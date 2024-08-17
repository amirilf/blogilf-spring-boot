package com.blogilf.blog.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.blogilf.blog.dto.ArticleDTO;
import com.blogilf.blog.exception.CustomConflictException;
import com.blogilf.blog.exception.CustomForbiddenException;
import com.blogilf.blog.exception.CustomResourceNotFoundException;
import com.blogilf.blog.model.Article;
import com.blogilf.blog.model.User;
import com.blogilf.blog.repository.ArticleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    
    private final ArticleRepository articleRepository;

    ArticleService(ArticleRepository articleRepository){
        this.articleRepository = articleRepository;
    }

    public List<ArticleDTO> getArticles(){
        return articleRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Article createArticle(Article article) {
        
        if (articleRepository.existsBySlug(article.getSlug())){
            throw new CustomConflictException("Slug is already used.");
        }
        
        return articleRepository.save(article);
    }

    public Article getArticle(String slug) {

        Optional<Article> article = articleRepository.findBySlug(slug);
        
        if (article.isPresent()) {
            return article.get();            
        }

        throw new CustomResourceNotFoundException("No article found.");
    }

    
    public Article updateArticle(String slug,Article article){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Article> updatedArticleOptional = articleRepository.findBySlug(slug);
        
        if (!username.equals(updatedArticleOptional.get().getAuthor().getUsername())) {
            throw new CustomForbiddenException("User is not the author.");
        }
        
        if (updatedArticleOptional.isEmpty()){
            throw new CustomResourceNotFoundException("No article found.");
        }

        if (!slug.equals(article.getSlug()) && articleRepository.existsBySlug(article.getSlug())){

            // TODO: remove this or not
            // set slug to the original one!
            // article.setSlug(slug);

            throw new CustomConflictException("Slug is already used.");
        }
        
        Article updatedArticle = updatedArticleOptional.get();

        updatedArticle.setSlug(article.getSlug());
        updatedArticle.setTitle(article.getTitle());
        updatedArticle.setContent(article.getContent());

        return articleRepository.save(updatedArticle);
    }

    private ArticleDTO convertToDto(Article article) {
        
        ArticleDTO dto = new ArticleDTO();
        dto.setTitle(article.getTitle());
        dto.setSlug(article.getSlug());
        dto.setContent(article.getContent());
        dto.setPublishedDate(article.getPublishedDate());

        User user = article.getAuthor();
        dto.setAuthorName(user.getName());
        dto.setAuthorUsername(user.getUsername());

        return dto;
    }

}
