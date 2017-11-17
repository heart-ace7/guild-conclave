package io.hub.guild.service.article;

import com.google.common.collect.Lists;
import io.hub.guild.model.entity.article.Article;
import io.hub.guild.model.entity.article.ArticleCategory;
import io.hub.guild.model.entity.article.TreeArticleCategory;
import io.hub.guild.model.internal.article.ArticleAndCategoryDto;
import io.hub.guild.model.internal.article.ArticleAndCategoryDto.ArticleDto;
import io.hub.guild.repository.article.ArticleCategoryRepository;
import io.hub.guild.repository.article.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleCategoryRepository articleCategoryRepository;

    public List<ArticleAndCategoryDto> fetchArticles(final Long guildId) {
        // TODO add condition `guild id`.
        final List<TreeArticleCategory> treeCategories = articleCategoryRepository.findAllAsTree();
        final Map<Long, List<Article>> articles = articleRepository.findAllAsMap().stream()
                .collect(Collectors.groupingBy(Article::getCategoryId));

        return treeCategories.stream()
                .map(category -> createArticleAndCategoryDto(category, articles))
                .collect(Collectors.toList());
    }

    public ArticleAndCategoryDto fetchArticles(final Long guildId, final Long categoryId) {
        // TODO add condition `guild id`.
        final TreeArticleCategory treeCategory = articleCategoryRepository.findOneAsTree(categoryId);
        final Map<Long, List<Article>> articles = articleRepository.findAllAsMap().stream()
                .collect(Collectors.groupingBy(Article::getCategoryId));

        return createArticleAndCategoryDto(treeCategory, articles);
    }

    public Article fetchArticle(final Long guildId,
                                final Long articleId) {
        // TODO add condition `guild id`.
        final Article article = articleRepository.findById(articleId);

        if (article == null) {
            throw new RuntimeException("resource not found.");
        }
        return article;
    }

    @Transactional
    public void createArticle(final Long categoryId,
                              final String title,
                              final String content) {
        final Article article = new Article();

        article.setTitle(title);
        article.setContent(content);
        article.setCreatedAt(LocalDateTime.now());
        article.setCategoryId(categoryId);
        article.setPriority(-1L);  //FIXME

        final int count = articleRepository.insert(article);
        if (count != 1) {
            throw new RuntimeException("invalid insert count.");
        }
    }

    @Transactional
    public void updateArticle(final Long articleId,
                              final Long categoryId,
                              final String title,
                              final String content) {
        final Article article = articleRepository.findById(articleId);

        if (article == null) {
            throw new RuntimeException("resource not found.");
        }
        article.setTitle(title);
        article.setContent(content);
        article.setUpdatedAt(LocalDateTime.now());
        article.setCategoryId(categoryId);
        article.setPriority(-1L);  //FIXME

        final int count = articleRepository.update(article);
        if (count != 1) {
            throw new RuntimeException("invalid update count.");
        }
    }

    public TreeArticleCategory fetchCategoriesBy(final Long articleId) {
        return articleCategoryRepository.findOneAsTree(articleId);
    }

    public ArticleCategory fetchCategoryBy(final Long categoryId) {
        return articleCategoryRepository.findOne(categoryId);
    }

    /**
     * 記事とカテゴリーのdtoを作成します.
     *
     * @param category   カテゴリーentity
     * @param articleMap <カテゴリーID, 記事リスト>のmap
     * @return 記事とカテゴリーdto
     */
    private ArticleAndCategoryDto createArticleAndCategoryDto(final TreeArticleCategory category, final Map<Long, List<Article>> articleMap) {
        Assert.notNull(category, "TreeArticleCategory must be non-null.");
        Assert.notNull(articleMap, "Articles must be non-null.");

        final List<ArticleDto> articleDtos = Lists.newArrayList();
        final List<Article> articles = articleMap.get(category.getId());
        if (!CollectionUtils.isEmpty(articles)) {
            articles.stream()
                    .map(entity -> new ArticleDto(entity.getId(), entity.getTitle()))
                    .forEach(articleDtos::add);
        }
        final List<ArticleAndCategoryDto> subCategories = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(category.getSubCategories())) {
            // create nested node recursive
            category.getSubCategories().stream()
                    .map(subCategory -> this.createArticleAndCategoryDto(subCategory, articleMap))
                    .forEach(subCategories::add);
        }
        final ArticleAndCategoryDto dto = new ArticleAndCategoryDto();
        dto.setCategoryId(category.getId());
        dto.setCategoryName(category.getName());
        dto.setArticles(articleDtos);
        dto.setSubCategories(subCategories);

        return dto;
    }

}
