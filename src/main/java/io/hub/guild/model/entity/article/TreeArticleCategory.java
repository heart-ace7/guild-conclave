package io.hub.guild.model.entity.article;

import lombok.Data;

import java.util.List;

/**
 * 隣接リスト型で取り出したカテゴリ-記事を持つentityクラス.
 */
@Data
public class TreeArticleCategory {

    private Long categoryId;

    private String categoryName;

    private Long subCategoryId;

    private String subCategoryName;

    private List<String> articleTitles;
}
