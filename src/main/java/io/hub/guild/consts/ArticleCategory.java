package io.hub.guild.consts;

import lombok.Getter;

@Getter
public enum ArticleCategory {
    CAPTURE(1L, "攻略"),
    COMMUNITY(2L, "コミュニティ"),
    DATABASE(3L, "データベース"),
    OTHER(99L, "その他");

    private final Long id;
    private final String categoryName;

    ArticleCategory(final Long id, final String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
}
