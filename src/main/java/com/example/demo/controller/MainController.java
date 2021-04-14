package com.example.demo.controller;

import com.example.demo.models.entity.Users;
import com.example.demo.models.repository.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private UsersRepository ur;

    @RequestMapping("/")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/welcome")
    public String profile(ModelMap model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser principal = ((OidcUser) authentication.getPrincipal());

        }
        return "welcome";
    }
}