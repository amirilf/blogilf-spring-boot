package com.blogilf.blog.model.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private String title;
    private String slug;
    private String content;
    private LocalDate publishedDate;
    private String authorName;
    private String authorUsername;
}