package io.hub.guild.model.internal.article;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class ArticleAndCategoryDto {
    private Long categoryId;

    private String categoryName;

    private List<ArticleDto> articles;

    private List<ArticleAndCategoryDto> subCategories;

    @Data
    @AllArgsConstructor
    public static class ArticleDto {
        private Long id;

        private String title;
    }
}
