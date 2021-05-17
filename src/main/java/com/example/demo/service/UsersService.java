package com.example.demo.service;


import com.example.demo.models.entity.UserProfile;
import com.example.demo.models.repository.UserProfileRepository;
import com.example.demo.utls.HTTPClientGame;
import com.example.demo.utls.MyJsonParser;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UsersService {
    private final UserProfileRepository userProfileRepository;


    @Autowired
    public UsersService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    } /// может просто заавтоварить?

    public List<String> getSteamUserNameAndAvatarUrl(UserProfile users)
    {
        List<String> userInfo = new ArrayList<>();
        String openID = getOpenId(users);
        try{
            String userUrl = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=6DB0B555FA0F62FD7622E385682AADB2&steamids=" + openID;
            String gameString = new HTTPClientGame(userUrl).getAll();
            userInfo = new MyJsonParser().parseUserInfoList(gameString);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return userInfo;
    }


    private String getOpenId(UserProfile users) {
        //возможно тут надо было через реп
        String openIdUrl = users.getId();
        return openIdUrl.substring(36);
    }

    //method for updating user;
    public UserProfile findSteamInfo(UserProfile users) {
        List<String> userInfo =  getSteamUserNameAndAvatarUrl(users);
        users.setName(userInfo.get(0));
        users.setSmallAvatarUrl(userInfo.get(1));
        users.setMediumAvatarUrl(userInfo.get(2));
        users.setFullAvatarUrl(userInfo.get(3));
        return users;
    }

    public UserProfile updateSteamInfo(UserProfile users)
    {
        Optional<UserProfile> usersOptional = userProfileRepository.findById(users.getId());
        if (usersOptional.isPresent())users=usersOptional.get();
        else users= null;
        users = findSteamInfo(users);
        return userProfileRepository.save(users);
    }

    public void setTradeTokenAndParnerID(String tradeUrl, UserProfile user)
    {
        user.setTradeUrl(tradeUrl);
        String partner= tradeUrl.substring(tradeUrl.indexOf("=")+1,tradeUrl.indexOf("&"));
        String token= tradeUrl.substring(tradeUrl.lastIndexOf("=")+1);
        user.setTradeToken(token);
        user.setPartnerID(partner);
        userProfileRepository.save(user);

    }
}
