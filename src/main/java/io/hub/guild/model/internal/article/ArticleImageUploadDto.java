package io.hub.guild.model.internal.article;

import lombok.Data;

@Data
public class ArticleImageUploadDto {

    private String directoryPath;

    private String fileName;

    private String extension;
}
