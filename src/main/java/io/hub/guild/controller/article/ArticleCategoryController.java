package io.hub.guild.controller.article;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ArticleCategoryController {

    @GetMapping("guilds/{guildId}/article-categories")
    public ModelAndView index() {


        return new ModelAndView();
    }
}
