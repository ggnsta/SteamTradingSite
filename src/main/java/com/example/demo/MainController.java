package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @GetMapping("/SteamAuthRedirect")
    public String stemAuthRedirect(Model model) {
        return "SteamAuthRedirect";
    }

    @GetMapping("/test")
    public String ttest(Model model) {
        return "test";
    }

}