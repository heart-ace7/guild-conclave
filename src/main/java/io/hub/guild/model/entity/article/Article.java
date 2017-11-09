package io.hub.guild.model.entity.article;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
