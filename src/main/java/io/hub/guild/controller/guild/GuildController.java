package io.hub.guild.controller.guild;

import com.google.common.collect.ImmutableMap;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

@Controller
public class GuildController {

    @GetMapping("guilds/{guildId}")
    @Secured("ROLE_USER")
    public ModelAndView index(final Principal principal) {
        final Map<String, Object> params = ImmutableMap.of(
                "guildName", "FFL"
        );

        return new ModelAndView("guild/index", params);
    }
}
