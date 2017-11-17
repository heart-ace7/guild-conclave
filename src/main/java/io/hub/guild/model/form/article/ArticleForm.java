package io.hub.guild.model.form.article;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ArticleForm {
    @NotNull(message = "必ず選択してください.")
    private Long categoryId;

    @NotNull(message = "必ず選択してください.")
    private Long subCategoryId;

    @Length(min = 1, max = 255)
    private String title;

    @Length(min = 1, max = 5_000)
    @Pattern(regexp = "[^<>$'\"]*")
    private String content;
}
