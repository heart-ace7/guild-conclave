package io.hub.guild.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(final Principal principal) {
        return "forward:/guilds/1";
    }
}
