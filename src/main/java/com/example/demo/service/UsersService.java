package com.example.demo.service;
import com.example.demo.models.entity.UserProfile;
import com.example.demo.models.repository.UserProfileRepository;
import com.example.demo.utls.HTTPClientGame;
import com.example.demo.utls.MyJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UsersService {
    private final UserProfileRepository userProfileRepository;


    @Autowired
    public UsersService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public List<String> getSteamUserNameAndAvatarUrl(UserProfile user)
    {
        List<String> userInfo = new ArrayList<>();
        String openID = user.getId();
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

    public UserProfile findSteamInfo(UserProfile user) {
        List<String> userInfo =  getSteamUserNameAndAvatarUrl(user);
        user.setName(userInfo.get(0));
        user.setSmallAvatarUrl(userInfo.get(1));
        user.setMediumAvatarUrl(userInfo.get(2));
        user.setFullAvatarUrl(userInfo.get(3));
        return user;
    }

    public UserProfile updateSteamInfo(UserProfile users)
    {
        users = findSteamInfo(users);
        return userProfileRepository.save(users);
    }

    public void setTradeTokenAndPartnerID(String tradeUrl, UserProfile user)
    {
        user.setTradeUrl(tradeUrl);
        String partner= tradeUrl.substring(tradeUrl.indexOf("=")+1,tradeUrl.indexOf("&"));
        String token= tradeUrl.substring(tradeUrl.lastIndexOf("=")+1);
        user.setTradeToken(token);
        user.setPartnerID(partner);
        userProfileRepository.save(user);

    }
}
