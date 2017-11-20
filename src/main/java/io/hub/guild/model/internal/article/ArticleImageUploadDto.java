package io.hub.guild.model.internal.article;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleImageUploadDto {

    private String directoryPath;

    private String fileName;

    private String extension;
}
