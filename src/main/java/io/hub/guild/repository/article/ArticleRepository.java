package io.hub.guild.repository.article;

import io.hub.guild.model.entity.article.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleRepository {
    List<Article> findAll();

    Article findById(@Param("articleId") final Long id);

    int insert(@Param("entity") final Article article);

    int update(@Param("entity") final Article article);
}
