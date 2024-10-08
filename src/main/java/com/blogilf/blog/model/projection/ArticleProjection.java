package com.blogilf.blog.model.projection;

import java.time.LocalDate;

public interface ArticleProjection {
    String getTitle();
    String getSlug();
    LocalDate getPublishedDate();
    Long getView();
    Integer getReadTime();
    String getAuthorUsername();
    String getAuthorName();
}