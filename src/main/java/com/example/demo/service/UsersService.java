package com.example.demo.service;


import com.example.demo.models.entity.UsersProfile;
import com.example.demo.models.repository.UsersRepository;
import com.example.demo.utls.HTTPClientGame;
import com.example.demo.utls.MyJsonParser;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.util.*;


@Service
public class UsersService {
    private final UsersRepository usersRepository;


    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<String> getSteamUserNameAndAvatarUrl(UsersProfile users)
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


    private String getOpenId(UsersProfile users) {
        //возможно тут надо было через реп
        String openIdUrl = users.getId();
        return openIdUrl.substring(36);
    }

    //method for updating user;
    public UsersProfile findSteamInfo(UsersProfile users) {
        List<String> userInfo =  getSteamUserNameAndAvatarUrl(users);
        users.setName(userInfo.get(0));
        users.setSmallAvatarUrl(userInfo.get(1));
        users.setMediumAvatarUrl(userInfo.get(2));
        users.setFullAvatarUrl(userInfo.get(3));
        return users;
    }

    public UsersProfile updateSteamInfo(UsersProfile users)
    {
        Optional<UsersProfile> usersOptional = usersRepository.findById(users.getId());
        if (usersOptional.isPresent())users=usersOptional.get();
        else users= null;
        users = findSteamInfo(users);
        return usersRepository.save(users);
    }
}
