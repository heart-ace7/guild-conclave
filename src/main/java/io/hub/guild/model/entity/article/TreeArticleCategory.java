package io.hub.guild.model.entity.article;

import lombok.Data;

import java.util.List;

/**
 * ツリー階層の記事カテゴリentityクラス.
 */
@Data
public class TreeArticleCategory {
    private Long id;

    private String name;

    private List<TreeArticleCategory> subCategories;
}
