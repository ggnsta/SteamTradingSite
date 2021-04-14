package com.example.demo.controller;

import com.example.demo.models.entity.Users;
import com.example.demo.models.repository.UsersRepository;
import com.example.demo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private UsersRepository userRepository;

    @RequestMapping("/")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/welcome")
    public String profile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((!(authentication  instanceof AnonymousAuthenticationToken)) && authentication  != null) {
            UserDetails userDetail = (UserDetails) authentication .getPrincipal();


            if (userDetail != null) {
                String openIdUrl = ((Users) authentication .getPrincipal()).getId();
                Optional<Users> usersOptional = userRepository.findById(openIdUrl);
                Users user;
                user=usersOptional.orElse(usersOptional.get());
                UsersService usersService = new UsersService(userRepository);
                usersService.updateSteamInfo(user);
                System.out.println(user.getName());
                model.addAttribute("username", user.getName());
            } else {
                model.addAttribute("username", "");
            }
        }
        return "welcome";
    }
}