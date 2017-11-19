package io.hub.guild.controller.article;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.hub.guild.model.entity.article.Article;
import io.hub.guild.model.entity.article.ArticleCategory;
import io.hub.guild.model.form.article.ArticleForm;
import io.hub.guild.model.internal.article.ArticleAndCategoryDto;
import io.hub.guild.model.view.article.ArticleIndexDto;
import io.hub.guild.model.view.article.ArticleIndexDto.ArticleDto;
import io.hub.guild.model.view.article.ArticleIndexDto.PrimaryCategoryDto;
import io.hub.guild.model.view.article.ArticleIndexDto.SecondaryCategoryDto;
import io.hub.guild.model.view.article.ArticleShowDto;
import io.hub.guild.service.article.ArticleService;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Secured("ROLE_USER")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * ギルドの記事一覧を表示します.
     *
     * @param guildId
     * @return
     */
    @GetMapping("guilds/{guildId}/articles")
    public ModelAndView index(@PathVariable final Long guildId) {
        final List<ArticleAndCategoryDto> articles = articleService.fetchArticles(guildId);

        final List<PrimaryCategoryDto> categories = articles.stream()
                .map(dto -> {
                    final List<SecondaryCategoryDto> secondaries = dto.getSubCategories().stream()
                            .map(subCategory -> {
                                final List<ArticleDto> articleDtos = Lists.newArrayList();

                                if (!CollectionUtils.isEmpty(subCategory.getArticles())) {
                                    subCategory.getArticles().stream()
                                            .map(article -> new ArticleDto(article.getId(), article.getTitle()))
                                            .forEach(articleDtos::add);
                                }
                                return new SecondaryCategoryDto(subCategory.getCategoryName(), articleDtos);
                            })
                            .collect(Collectors.toList());

                    return new PrimaryCategoryDto(dto.getCategoryId(), dto.getCategoryName(), secondaries);
                })
                .collect(Collectors.toList());

        return new ModelAndView("article/index",
                "model", new ArticleIndexDto(guildId, "FFL", categories));
    }

    /**
     * ギルド記事の詳細を表示します.
     *
     * @param guildId
     * @param articleId
     * @return
     */
    @GetMapping("guilds/{guildId}/articles/{articleId}")
    public ModelAndView show(@PathVariable final Long guildId, @PathVariable final Long articleId) {
        final Article article = articleService.fetchArticle(guildId, articleId);

        final PegDownProcessor processor = new PegDownProcessor(Extensions.ALL); // not thread-safe
        final ArticleShowDto.ArticleDto dto = new ArticleShowDto.ArticleDto();
        dto.setTitle(article.getTitle());
        dto.setHtmlContent(processor.markdownToHtml(article.getContent()));

        final ArticleShowDto viewDto = new ArticleShowDto();
        viewDto.setGuildId(guildId);
        viewDto.setGuildName("FFL");
        viewDto.setArticle(dto);

        return new ModelAndView("article/show", "model", viewDto);
    }

    /**
     * ギルド記事の入力画面を表示します.
     *
     * @param guildId
     * @param articleForm
     * @return
     */
    @GetMapping("guilds/{guildId}/articles/input")
    public ModelAndView input(@PathVariable final Long guildId, final ArticleForm articleForm) {
        return renderInput(guildId, articleForm.getCategoryId());
    }

    /**
     * ギルド記事を登録します.
     *
     * @param guildId
     * @param articleForm
     * @param bindingResult
     * @return
     */
    @PostMapping("guilds/{guildId}/articles/create")
    public ModelAndView create(@PathVariable final Long guildId, @Valid final ArticleForm articleForm, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return renderInput(guildId, articleForm.getCategoryId());
        }
        articleService.createArticle(articleForm.getSubCategoryId(), articleForm.getTitle(), articleForm.getContent());

        return new ModelAndView("redirect:/guilds/" + guildId + "/articles");
    }

    /**
     * ギルド記事の編集画面を表示します.
     *
     * @param guildId
     * @param articleId
     * @param articleForm
     * @return
     */
    @GetMapping("guilds/{guildId}/articles/{articleId}/edit")
    public ModelAndView edit(@PathVariable final Long guildId, @PathVariable final Long articleId, final ArticleForm articleForm) {
        final Article article = articleService.fetchArticle(guildId, articleId);
        final ArticleCategory category = articleService.fetchCategoryBy(article.getCategoryId());

        articleForm.setCategoryId(category.getParentId());
        articleForm.setSubCategoryId(category.getId());
        articleForm.setTitle(article.getTitle());
        articleForm.setContent(article.getContent());

        return renderEdit(guildId, articleForm.getCategoryId());
    }

    /**
     * ギルド記事を更新します.
     *
     * @param guildId
     * @param articleId
     * @param articleForm
     * @param bindingResult
     * @return
     */
    @PutMapping("guilds/{guildId}/articles/{articleId}/update")
    public ModelAndView update(@PathVariable final Long guildId, @PathVariable final Long articleId, @Valid final ArticleForm articleForm, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return renderEdit(guildId, articleForm.getCategoryId());
        }
        articleService.updateArticle(articleId, articleForm.getSubCategoryId(), articleForm.getTitle(), articleForm.getContent());

        return new ModelAndView("redirect:/guilds/" + guildId + "/articles");
    }

    private ModelAndView renderInput(final Long guildId, final Long categoryId) {
        final Map<String, Object> params = ImmutableMap.of(
                "guildName", "FFL",
                "category", articleService.fetchCategoriesBy(categoryId)
        );
        return new ModelAndView("article/input", params);
    }

    private ModelAndView renderEdit(final Long guildId, final Long categoryId) {
        final Map<String, Object> params = ImmutableMap.of(
                "guildName", "FFL",
                "category", articleService.fetchCategoriesBy(categoryId)
        );
        return new ModelAndView("article/edit", params);
    }
}
