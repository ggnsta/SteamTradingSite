package com.example.demo.controller;

import com.example.demo.models.entity.Users;
import com.example.demo.models.repository.UsersRepository;
import com.example.demo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class ProfileController {
    private final UsersRepository usersRepository;

    @Autowired
    public ProfileController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/profile-info")
    public String profile (Principal principal, Model model)
    {

            System.out.println("principal:"+principal);
            System.out.println("principal_getname:"+principal.getName());
            String openId = principal.getName();
            Users users;
            Optional<Users> usersOptional = usersRepository.findById(openId);
            if (usersOptional.isPresent())users=usersOptional.get();
            else users= null;
            UsersService us = new UsersService(usersRepository);
            us.updateSteamInfo(users);
            model.addAttribute("name", users.getName());

            return "profile-info";

    }
}