package io.hub.guild.service.article;

import io.hub.guild.model.entity.article.Article;
import io.hub.guild.repository.article.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> fetchArticle(final Long guildId) {
        // TODO find by guild.

        return articleRepository.findAll();
    }

    public Article fetchArticle(final Long guildId, final Long articleId) {
        // TODO find by guild.
        final Article article = articleRepository.findById(articleId);

        if (article == null) {
            throw new RuntimeException("resource not found.");
        }
        return article;
    }

    @Transactional
    public void createArticle(final String title, final String content) {
        final Article article = new Article();

        article.setTitle(title);
        article.setContent(content);
        article.setCreatedAt(LocalDateTime.now());

        final int count = articleRepository.insert(article);
        if (count != 1) {
            throw new RuntimeException("invalid insert count.");
        }
    }

    @Transactional
    public void updateArticle(final Long articleId, final String title, final String content) {
        final Article article = articleRepository.findById(articleId);

        if (article == null) {
            throw new RuntimeException("resource not found.");
        }
        article.setTitle(title);
        article.setContent(content);
        article.setUpdatedAt(LocalDateTime.now());

        final int count = articleRepository.update(article);
        if (count != 1) {
            throw new RuntimeException("invalid update count.");
        }
    }

}
