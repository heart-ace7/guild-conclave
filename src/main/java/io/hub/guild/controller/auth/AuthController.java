package io.hub.guild.controller.auth;

import com.google.common.collect.ImmutableMap;
import io.hub.guild.model.form.auth.AuthForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.security.web.WebAttributes.AUTHENTICATION_EXCEPTION;

@Controller
public class AuthController {

    @RequestMapping({"login", "login-fail"})
    public ModelAndView login(@RequestAttribute(name = AUTHENTICATION_EXCEPTION, required = false) Exception exception,
                              final AuthForm authForm) {

        return new ModelAndView("auth/login", ImmutableMap.of(
                "error", (exception != null)
        ));
    }

}
