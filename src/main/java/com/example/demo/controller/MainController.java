package com.example.demo.controller;

import com.example.demo.models.entity.Skins;
import com.example.demo.models.entity.UserProfile;
import com.example.demo.models.repository.SkinPriceRepository;
import com.example.demo.models.repository.SkinsRepository;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Autowired
    private SkinsRepository skinsRepository;

    private UserProfile user;


    @RequestMapping ("/Trades")
    public  String trades (Model model)  {
        tradeService.startTradeService();
        taskScheduler.scheduleWithFixedDelay(tradeService,10000);

        return "welcome";
    }

    @RequestMapping ("/test")
    public  String testtest (Model model)  {


        return "test";
    }

    @RequestMapping("/search")
    public String test (Model model)
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
                this.user=user;
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
        model.addAttribute("smallImgURL",user.getSmallAvatarUrl());

        return "profile-info";
    }


    @RequestMapping("/inventory")
    public String getInventory(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication .getPrincipal();
        String openIdUrl = ((UserProfile) authentication .getPrincipal()).getId();
        Optional<UserProfile> usersOptional = userRepository.findById(openIdUrl);
        UserProfile user2;
        user2=usersOptional.orElse(usersOptional.get());
        inventoryService.updateUserSkinsDatabase(user2);
       // System.out.println(inventoryService.calculateInventoryCost(user2.getSkins()));
        List <Skins> skinsList = new ArrayList<Skins>();
        skinsList=user2.getSkins();
        model.addAttribute("skinsList", skinsList);
        return "inventory";
    }

    @PostMapping("/inventory/add")
    public String getSkinsListToTrade(@RequestBody List<String> selectedSkinId)  {
        List<Skins> skinsToTrade= new ArrayList<Skins>();
        for (int i =0; i<selectedSkinId.size();i++)
        {
            Optional<Skins> skinsOptional =skinsRepository.findById(selectedSkinId.get(i));
            skinsToTrade.add(skinsOptional.orElse(skinsOptional.get()));
        }

        tradeService.sendTradeOffer(new ArrayList<Skins>(),skinsToTrade,user);

        return "redirect:/profile-info";
    }

}