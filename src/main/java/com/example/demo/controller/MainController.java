package com.example.demo.controller;

import com.example.demo.Bot.BotLogin;
import com.example.demo.models.entity.BotDetails;
import com.example.demo.models.entity.Skins;
import com.example.demo.models.entity.UsersProfile;
import com.example.demo.models.repository.BotDetailsRepository;
import com.example.demo.models.repository.UsersRepository;
import com.example.demo.service.InventoryService;
import com.example.demo.service.TradeService;
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

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private TradeService tradeService;

    @RequestMapping ("/Trades")
    public  String trades (Model model) throws IOException {
        tradeService.hello();
        return "Trades";
    }

    @RequestMapping("/welcome")
    public String profile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((!(authentication  instanceof AnonymousAuthenticationToken)) && authentication  != null) {
            UserDetails userDetail = (UserDetails) authentication .getPrincipal();

            if (userDetail != null) {
                String openIdUrl = ((UsersProfile) authentication .getPrincipal()).getId();
                Optional<UsersProfile> usersOptional = userRepository.findById(openIdUrl);
                UsersProfile user;
                user=usersOptional.orElse(usersOptional.get());
                usersService.updateSteamInfo(user);
                System.out.println(user.getName());
                model.addAttribute("username", user.getName());
            } else {
                model.addAttribute("username", "");
            }
        }
        return "welcome";
    }

    @RequestMapping("/profile-info")
    public String govno() throws IOException, InterruptedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication .getPrincipal();
        String openIdUrl = ((UsersProfile) authentication .getPrincipal()).getId();
        Optional<UsersProfile> usersOptional = userRepository.findById(openIdUrl);
        UsersProfile user;
        user=usersOptional.orElse(usersOptional.get());
        inventoryService.updateUserSkinsDatabase(user);
        return "profile-info";
    }



}