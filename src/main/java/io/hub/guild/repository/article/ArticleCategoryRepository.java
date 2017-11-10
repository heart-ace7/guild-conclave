package io.hub.guild.repository.article;

import io.hub.guild.model.entity.article.TreeArticleCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleCategoryRepository {

    List<TreeArticleCategory> findAllAsTree();

}
