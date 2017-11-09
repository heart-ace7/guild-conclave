package io.hub.guild.model.view.article;

import lombok.Data;

@Data
public class ArticleShowDto {

    private Long guildId;

    private String guildName;

    private ArticleDto article;

    @Data
    public static class ArticleDto {
        private String title;

        private String htmlContent;
    }
}
