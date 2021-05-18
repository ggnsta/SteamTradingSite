package com.example.demo.controller;

import com.example.demo.models.entity.Skins;
import com.example.demo.models.entity.UserProfile;
import com.example.demo.models.repository.SkinPriceRepository;
import com.example.demo.models.repository.TradeOfferRepository;
import com.example.demo.models.repository.UserProfileRepository;
import com.example.demo.service.InventoryService;
import com.example.demo.service.SkinPriceService;
import com.example.demo.service.TradeService;
import com.example.demo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private UserProfileRepository userRepository;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private SkinPriceService skinPriceService;
    @Autowired
    private SkinPriceRepository skinPriceRepository;
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private TradeOfferRepository tradeOfferRepository;


    @RequestMapping ("/Trades")
    public  String trades (Model model)  {
        tradeService.main();
        tradeService.sendTradeOffer();
        taskScheduler.scheduleWithFixedDelay(tradeService,10000);

        return "Trades";
    }


    @RequestMapping("/search")
    public String huevo (Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication .getPrincipal();
        String openIdUrl = ((UserProfile) authentication .getPrincipal()).getId();
        Optional<UserProfile> usersOptional = userRepository.findById(openIdUrl);
        UserProfile user;
        user=usersOptional.orElse(usersOptional.get());
        skinPriceService.requestOneSkinPrice(1,"Chroma 3 Case");

        return "";
    }

    @RequestMapping("/welcome")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((!(authentication  instanceof AnonymousAuthenticationToken)) && authentication  != null) {
            UserDetails userDetail = (UserDetails) authentication .getPrincipal();

            if (userDetail != null) {
                String openIdUrl = ((UserProfile) authentication .getPrincipal()).getId();
                Optional<UserProfile> usersOptional = userRepository.findById(openIdUrl);
                UserProfile user;
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

    @PostMapping("/profile-info/setTradeUrl")
    public String setUserTradeUrl(@RequestParam String tradeUrlArea, Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication .getPrincipal();
        String openIdUrl = ((UserProfile) authentication .getPrincipal()).getId();
        Optional<UserProfile> usersOptional = userRepository.findById(openIdUrl);
        UserProfile user;
        user=usersOptional.orElse(usersOptional.get());
        usersService.setTradeTokenAndParnerID(tradeUrlArea, user);
        return "redirect:/profile-info";
    }
    @RequestMapping("/profile-info")
    public String getUsersProfileInfo(Model model)  {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String openIdUrl = ((UserProfile) authentication .getPrincipal()).getId();
        Optional<UserProfile> usersOptional = userRepository.findById(openIdUrl);
        UserProfile user;
        user=usersOptional.orElse(usersOptional.get());

        model.addAttribute("steamNickname", user.getName());
        model.addAttribute("joinDateTime", user.getJoinDateTime());
       // model.addAttribute("countOfTrades", user.getCountOftrades());
        model.addAttribute("mediumImgURL", user.getMediumAvatarUrl());
        model.addAttribute("tradeUrl",user.getTradeUrl());

        return "profile-info";
    }


    @RequestMapping("/inventory")
    public String getInventory()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication .getPrincipal();
        String openIdUrl = ((UserProfile) authentication .getPrincipal()).getId();
        Optional<UserProfile> usersOptional = userRepository.findById(openIdUrl);
        UserProfile user;
        user=usersOptional.orElse(usersOptional.get());
        inventoryService.updateUserSkinsDatabase(user);
        System.out.println(inventoryService.calculateInventoryCost(user.getSkins()));
        return "profile-info";
    }

}