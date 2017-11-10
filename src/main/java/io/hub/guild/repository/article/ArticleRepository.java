package io.hub.guild.repository.article;

import io.hub.guild.model.entity.article.Article;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleRepository {

    @MapKey("categoryId")
    Map<Long, List<Article>> findAllAsMap();

    Article findById(@Param("articleId") final Long id);

    int insert(@Param("entity") final Article article);

    int update(@Param("entity") final Article article);
}
