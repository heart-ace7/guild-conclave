package io.hub.guild.model.entity.article;

import lombok.Data;

@Data
public class ArticleCategory {

    private Long id;

    private Long parentId;

    private String name;

    private Long priority;
}
