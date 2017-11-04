package io.hub.guild.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {

    @GetMapping("/")
    public ModelAndView index(final Principal principal) {
        final Map<String, Object> params = new HashMap<>();

        return new ModelAndView("index", params);
    }
}
