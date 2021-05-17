package com.example.demo.models.entity;

import com.google.gson.JsonObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class BotDetails {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    private String steamLogin;
    private String tradeID;
    private String tradeToken;
    private String SharedSecret;
    private String IdentitySecret;
    private String DeviceID;
    private String password;
    private String cookies;
    private String transferParameters;
    @OneToMany(mappedBy = "botLogin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TradeOffer> tradeOffers;

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

    public String getTransferParameters() {
        return transferParameters;
    }

    public void setTransferParameters(String transferParameters) {
        this.transferParameters = transferParameters;
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

    public String getTradeID() {
        return tradeID;
    }

    public void setTradeID(String tradeID) {
        this.tradeID = tradeID;
    }

    public String getTradeToken() {
        return tradeToken;
    }

    public void setTradeToken(String tradeToken) {
        this.tradeToken = tradeToken;
    }
}
