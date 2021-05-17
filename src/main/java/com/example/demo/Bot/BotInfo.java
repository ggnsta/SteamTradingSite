package com.example.demo.Bot;

import com.example.demo.models.entity.BotDetails;
import com.example.demo.models.repository.BotDetailsRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class BotInfo {
    public  String steamLogin;
    public  String password;
    public  String tradeID;
    public  String tradeToken;
    public  String sharedSecret;
    public  String identitySecret;
    public  String deviceID;
    public String cookies;
  //  public JsonObject transferParameters;

    public BotInfo(String steamLogin, String password, String tradeID, String tradeToken, String sharedSecret, String identitySecret, String deviceID, String cookies) {
        this.steamLogin = steamLogin;
        this.password = password;
        this.tradeID = tradeID;
        this.tradeToken = tradeToken;
        this.sharedSecret = sharedSecret;
        this.identitySecret = identitySecret;
        this.deviceID = deviceID;
        this.cookies = cookies;
        //this.transferParameters = transferParameters;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }
}
