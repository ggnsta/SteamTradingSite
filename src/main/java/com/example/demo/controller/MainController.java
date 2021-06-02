package com.example.demo.controller;

import com.example.demo.models.entity.BotDetails;
import com.example.demo.models.entity.Skins;
import com.example.demo.models.entity.TradeOffer;
import com.example.demo.models.entity.UserProfile;
import com.example.demo.models.repository.*;
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
    @Autowired
    private BotDetailsRepository botDetailsRepository;

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
    public String welcome(Model model) {
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
                model.addAttribute("user",user);
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
        usersService.setTradeTokenAndPartnerID(tradeUrlArea, user);
        return "redirect:/profile-info";
    }
    @RequestMapping("/profile-info")
    public String getUsersProfileInfo(Model model)  {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String openIdUrl = ((UserProfile) authentication .getPrincipal()).getId();
        Optional<UserProfile> usersOptional = userRepository.findById(openIdUrl);
        UserProfile user;
        user=usersOptional.orElse(usersOptional.get());


        model.addAttribute("user",user);
        model.addAttribute("inventoryCost", inventoryService.calculateInventoryCost(user.getSkins()));

        return "profile-info";
    }

    @RequestMapping("/purchase")
    public String getBotInventory(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication .getPrincipal();
        String openIdUrl = ((UserProfile) authentication .getPrincipal()).getId();
        Optional<UserProfile> usersOptional = userRepository.findById(openIdUrl);
        UserProfile user;
        user=usersOptional.orElse(usersOptional.get());

        List<BotDetails> bots =botDetailsRepository.findAll();
        UserProfile botUserProfile;
        List <Skins> botsSkinsList=new ArrayList<>();
        for (int i=0;i<bots.size();i++)
        {
           botUserProfile= bots.get(i).getUserProfile();
           inventoryService.updateUserSkinsDatabase(botUserProfile);
           botsSkinsList.addAll(botUserProfile.getSkins());
        }


        model.addAttribute("skinsList", botsSkinsList);
        model.addAttribute("user",user);
        return "purchase";
    }

    @PostMapping("/purchase/trade")
    @ResponseBody
    public String getBotSkinsListToTrade(@RequestBody List<String> selectedSkinId)  {
        List<Skins> skinsToTrade= new ArrayList<Skins>();
        for (int i =0; i<selectedSkinId.size();i++)
        {
            Optional<Skins> skinsOptional =skinsRepository.findById(selectedSkinId.get(i));
            skinsToTrade.add(skinsOptional.orElse(skinsOptional.get()));
        }
        tradeService.startTradeService();
        String tradeOfferID = tradeService.sendTradeOffer(skinsToTrade,new ArrayList<Skins>(),user);

        Optional<TradeOffer> tradeOfferOptional=tradeOfferRepository.findById(tradeOfferID);
        TradeOffer tradeOffer = tradeOfferOptional.orElse(tradeOfferOptional.get());
        return tradeOffer.getMessage();
    }

    @RequestMapping("/selling")
    public String getUserInventory(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication .getPrincipal();
        String openIdUrl = ((UserProfile) authentication .getPrincipal()).getId();
        Optional<UserProfile> usersOptional = userRepository.findById(openIdUrl);
        UserProfile user;
        user=usersOptional.orElse(usersOptional.get());

        inventoryService.updateUserSkinsDatabase(user);


        List <Skins> skinsList;
        skinsList=user.getSkins();
        model.addAttribute("skinsList", skinsList);
        model.addAttribute("user",user);
        return "selling";
    }

    @PostMapping("/selling/trade")
    @ResponseBody
    public String getUserSkinsListToTrade(@RequestBody List<String> selectedSkinId)  {
        List<Skins> skinsToTrade= new ArrayList<Skins>();
        for (int i =0; i<selectedSkinId.size();i++)
        {
            Optional<Skins> skinsOptional =skinsRepository.findById(selectedSkinId.get(i));
            skinsToTrade.add(skinsOptional.orElse(skinsOptional.get()));
        }
        tradeService.startTradeService();
        String tradeOfferID = tradeService.sendTradeOffer(new ArrayList<Skins>(),skinsToTrade,user);

        Optional<TradeOffer> tradeOfferOptional=tradeOfferRepository.findById(tradeOfferID);
        TradeOffer tradeOffer = tradeOfferOptional.orElse(tradeOfferOptional.get());
        return tradeOffer.getMessage();
    }

}