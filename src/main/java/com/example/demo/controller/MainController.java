package com.example.demo.controller;

import com.example.demo.models.entity.Users;
import com.example.demo.models.repository.UsersRepository;
import com.example.demo.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private UsersRepository ur;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @GetMapping("/about")
    public String stemAuthRedirect(Model model) {
        return "about";
    }






}