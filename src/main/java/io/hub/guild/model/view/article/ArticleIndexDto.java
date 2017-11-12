package io.hub.guild.model.view.article;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ArticleIndexDto {

    private Long guildId;

    private String guildName;

    private List<PrimaryCategoryDto> categories;

    @Data
    @AllArgsConstructor
    public static class PrimaryCategoryDto {
        private String name;

        private List<SecondaryCategoryDto> secondaryCategories;
    }

    @Data
    @AllArgsConstructor
    public static class SecondaryCategoryDto {
        private String name;

        private List<ArticleDto> articles;
    }

    @Data
    @AllArgsConstructor
    public static class ArticleDto {
        private Long id;

        private String title;
    }
}
