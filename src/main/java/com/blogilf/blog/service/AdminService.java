package com.blogilf.blog.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.blogilf.blog.model.entity.Article;
import com.blogilf.blog.model.repository.ArticleRepository;
import com.blogilf.blog.model.specification.ArticleSpecification;

import java.util.List;
import java.time.LocalDate;

@Service
public class AdminService {

    private final ArticleRepository articleRepository;

    AdminService(ArticleRepository articleRepository){
        this.articleRepository = articleRepository;
    }
    
    public String AdminPanel() {
        return "Hey, this is Admin panel.";
    }

    public List<Article> getFilteredArticles(
            String title,
            String slug,
            String content,
            LocalDate dateBegin,
            LocalDate dateEnd,
            Long vmin,
            Long vmax,
            Integer rtmax,
            Integer rtmin) {

        Specification<Article> spec = Specification.where(null);

        if (title != null) spec = spec.and(ArticleSpecification.hasTitle(title));
        if (slug != null) spec = spec.and(ArticleSpecification.hasSlug(slug));
        if (content != null) spec = spec.and(ArticleSpecification.hasContent(content));
        
        if (dateBegin != null && dateEnd != null) spec = spec.and(ArticleSpecification.publishedDateBetween(dateBegin, dateEnd));
        else if (dateBegin != null) spec = spec.and(ArticleSpecification.publishedDateAfter(dateBegin));
        else if (dateEnd != null) spec = spec.and(ArticleSpecification.publishedDateBefore(dateEnd));
        
        if (vmin != null && vmax != null) spec = spec.and(ArticleSpecification.viewBetween(vmin, vmax));
        else if (vmin != null) spec = spec.and(ArticleSpecification.viewGreaterThan(vmin));
        else if (vmax != null) spec = spec.and(ArticleSpecification.viewLessThan(vmax));
        
        if (rtmin != null && rtmax != null) spec = spec.and(ArticleSpecification.readTimeBetween(rtmin, rtmax));
        else if (rtmin != null) spec = spec.and(ArticleSpecification.readTimeGreaterThan(rtmin));
        else if (rtmax != null) spec = spec.and(ArticleSpecification.readTimeLessThan(rtmax));

        return articleRepository.findAll(spec);
    }

}
