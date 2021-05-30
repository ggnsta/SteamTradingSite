package com.example.demo.Bot;

import com.example.demo.models.entity.BotDetails;
import com.example.demo.models.repository.BotDetailsRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class BotInfo {
    public  String steamID;
    public  String steamLogin;
    public  String password;
    public  String sharedSecret;
    public  String identitySecret;
    public  String deviceID;
    public  String cookies;
    public  String apiKey;

    public BotInfo(String steamID,String steamLogin, String password, String sharedSecret, String identitySecret, String deviceID, String cookies, String apiKey) {
        this.steamID = steamID;
        this.steamLogin = steamLogin;
        this.password = password;
        this.sharedSecret = sharedSecret;
        this.identitySecret = identitySecret;
        this.deviceID = deviceID;
        this.cookies = cookies;
        this.apiKey = apiKey;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }
}
