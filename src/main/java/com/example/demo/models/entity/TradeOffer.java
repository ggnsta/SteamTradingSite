package com.example.demo.models.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TradeOffer {

    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserProfile userProfileID;
    @ManyToOne(fetch = FetchType.LAZY)
    private BotDetails botLogin;
    private String message;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserProfile getUserProfileID() {
        return userProfileID;
    }

    public void setUserProfileID(UserProfile userProfileID) {
        this.userProfileID = userProfileID;
    }

    public BotDetails getBotLogin() {
        return botLogin;
    }

    public void setBotLogin(BotDetails botLogin) {
        this.botLogin = botLogin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
