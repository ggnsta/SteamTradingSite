package com.example.demo.models.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class BotDetails {
    @Id

    private String id;
    private String steamLogin;
    private String SharedSecret;
    private String IdentitySecret;
    private String DeviceID;
    private String password;
    private String cookies;
    private String apiKey;
    @OneToMany(mappedBy = "botLogin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TradeOffer> tradeOffers;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private UserProfile userProfile;

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public List<TradeOffer> getTradeOffers() {
        return tradeOffers;
    }

    public void setTradeOffers(List<TradeOffer> tradeOffers) {
        this.tradeOffers = tradeOffers;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSteamLogin() {
        return steamLogin;
    }

    public void setSteamLogin(String steamLogin) {
        this.steamLogin = steamLogin;
    }

    public String getSharedSecret() {
        return SharedSecret;
    }

    public void setSharedSecret(String sharedSecret) {
        SharedSecret = sharedSecret;
    }

    public String getIdentitySecret() {
        return IdentitySecret;
    }

    public void setIdentitySecret(String identitySecret) {
        IdentitySecret = identitySecret;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
