package com.blogilf.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blogilf.blog.exception.CustomResourceNotFoundException;
import com.blogilf.blog.model.entity.Article;
import com.blogilf.blog.model.entity.User;
import com.blogilf.blog.model.repository.ArticleRepository;
import com.blogilf.blog.model.repository.UserRepository;

@Service
public class SearchService {
    
    private final int searchPageSize = 5;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public SearchService(ArticleRepository articleRepository, UserRepository userRepository ) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<? extends Page<?>> search(String query, String type, String sort, int page) {
        if (type.equals("articles"))
            return searchArticles(query, sort, page);
        return searchUsers(query, sort, page);
    }
    
    public ResponseEntity<Page<User>> searchUsers(String query, String sort, int page) {

        Pageable pageable = PageRequest.of(page - 1, searchPageSize, getSort("dateJoined", sort));
        Page<User> usersPage =  userRepository.findByNameContainingOrUsernameContaining(query, query, pageable);

        if (page > usersPage.getTotalPages()) {
            throw new CustomResourceNotFoundException("No item found.");
        }
        return ResponseEntity.ok(usersPage);
    }

    public ResponseEntity<Page<Article>> searchArticles(String query, String sort, int page) {

        Pageable pageable = PageRequest.of(page - 1, searchPageSize, getSort("publishedDate", sort));
        Page<Article> articlesPage =  articleRepository.findByTitleContainingOrContentContaining(query, query, pageable);

        if (page > articlesPage.getTotalPages()) {
            System.out.println("here");
            throw new CustomResourceNotFoundException("No item found.");
        }
        return ResponseEntity.ok(articlesPage);
    }
    

    private Sort getSort(String field,String sort){
        if (sort.equals(sort)) {
            return Sort.by(field).ascending();
        }
        return Sort.by(field).descending();
    }

}
