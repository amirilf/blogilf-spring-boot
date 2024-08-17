// package com.blogilf.blog.service;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.atLeast;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.ui.Model;

// import com.blogilf.blog.model.Article;
// import com.blogilf.blog.repository.ArticleRepository;


// @ExtendWith(MockitoExtension.class)
// public class ArticleServiceTest {

//     @Mock
//     private ArticleRepository articleRepository;

//     @Mock
//     private Model model;

//     @InjectMocks
//     private ArticleService articleService;

//     private Article article = new Article(null,"Title","Slug","Content",null,null);

//     @Test
//     void createArticle_ReturnsCreated(){
        
//         // controlling how repo should answer
//         when(articleRepository.existsBySlug(article.getSlug())).thenReturn(false);

//         ResponseEntity<String> responseEntity = articleService.createArticle(article, model);

//         assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//         verify(articleRepository).save(any(Article.class));
//         verify(model,never()).addAttribute(anyString(),any());
//     }


//     @Test
//     void createArticle_ReturnsConflict(){

//         // controll repo to say yes it exists
//         when(articleRepository.existsBySlug(article.getSlug())).thenReturn(true);

//         ResponseEntity<String> responseEntity = articleService.createArticle(article, model);

//         assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
//         verify(articleRepository,never()).save(any(Article.class));
//         verify(model,atLeast(1)).addAttribute(anyString(),any(Article.class));
//     }


//     @Test
//     void createArticle_AddNewEmptyArticleToModel(){

//         articleService.createArticle(model);

//         verify(model).addAttribute(anyString(), any());
//     }

//     @Test
//     void getArticle_ReturnsFound(){

//         // controll repo to return article optional
//         when(articleRepository.findBySlug(article.getSlug())).thenReturn(Optional.of(article));

//         ResponseEntity<Void> responseEntity = articleService.getArticle(model, article.getSlug());

//         assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
//         verify(model).addAttribute(anyString(), any(Article.class));
//     }

//     @Test
//     void getArticle_ReturnsNotFound(){

//         when(articleRepository.findBySlug(article.getSlug())).thenReturn(Optional.empty());

//         ResponseEntity<Void> responseEntity = articleService.getArticle(model, article.getSlug());

//         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//         verify(model,never()).addAttribute(anyString(), any());
//     }


//     @Test
//     void getArticles_AddArticlesToModel(){

//         articleService.getArticles();

//         verify(articleRepository).findAll();
//         verify(model).addAttribute(anyString(), any(List.class));
//     }


//     @Test
//     void updateArticle_ReturnsNotFound(){

//         when(articleRepository.findBySlug("test")).thenReturn(Optional.empty());

//         ResponseEntity<String> responseEntity = articleService.updateArticle("test", article, model);

//         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//     }


//     @Test
//     void updateArticle_NewSlugIsAlreadyExisted_ReturnsConflict(){

//         // there is an existed article to update
//         when(articleRepository.findBySlug("test")).thenReturn(Optional.of(article));

//         // slug is already used
//         when(articleRepository.existsBySlug(article.getSlug())).thenReturn(true);

//         ResponseEntity<String> responseEntity = articleService.updateArticle("test", article, model);

//         assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
//         verify(model,times(2)).addAttribute(anyString(), any());
//     }

//     @Test
//     void updateArticle_ReturnsCreated(){

//         when(articleRepository.findBySlug("test")).thenReturn(Optional.of(article));

//         ResponseEntity<String> responseEntity = articleService.updateArticle("test", article, model);

//         assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//         verify(model,never()).addAttribute(anyString(), any());
//     }
// }

