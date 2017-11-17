package io.hub.guild.repository.article;

import io.hub.guild.model.entity.article.ArticleCategory;
import io.hub.guild.model.entity.article.TreeArticleCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleCategoryRepository {
    ArticleCategory findOne(@Param("id") Long id);

    TreeArticleCategory findOneAsTree(@Param("categoryId") Long id);

    List<TreeArticleCategory> findAllAsTree();
}
