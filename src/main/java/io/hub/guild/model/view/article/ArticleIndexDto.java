package io.hub.guild.model.view.article;

import lombok.Data;

import java.util.List;

@Data
public class ArticleIndexDto {

    private Long guildId;

    private String guildName;

    private List<ArticleDto> articles;

    @Data
    public static class ArticleDto {
        private String title;

        private String htmlContent;
    }
}
