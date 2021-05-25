package com.example.demo.models.entity;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
public class UserProfile implements UserDetails {
    @Id
    private String id;
    private String name;
    private BigDecimal balance;
    private String partnerID;
    private String tradeToken;
    private String smallAvatarUrl;
    private String mediumAvatarUrl;
    private String fullAvatarUrl;
    private LocalDateTime joinDateTime;
    private int countOfTrades=0;
    private String tradeUrl;
    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skins> skins;
    @OneToMany(mappedBy = "userProfileID", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TradeOffer> tradeOffers;



    public List<TradeOffer> getTradeOffers() {
        return tradeOffers;
    }

    public void setTradeOffers(List<TradeOffer> tradeOffers) {
        this.tradeOffers = tradeOffers;
    }

    public String getTradeUrl() {
        return tradeUrl;
    }

    public void setTradeUrl(String tradeUrl) {
        this.tradeUrl = tradeUrl;
    }

    public List<Skins> getSkins() {
        return skins;
    }

    public void setSkins(List<Skins> skins) {
        this.skins = skins;
    }

    public String getId() {
        return id;
    }

    public String getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(String partnerID) {
        this.partnerID = partnerID;
    }

    public String getTradeToken() {
        return tradeToken;
    }

    public void setTradeToken(String tradeToken) {
        this.tradeToken = tradeToken;
    }

    public String getFullAvatarUrl() {
        return fullAvatarUrl;
    }

    public void setFullAvatarUrl(String fullAvatarUrl) {
        this.fullAvatarUrl = fullAvatarUrl;
    }

    public LocalDateTime getJoinDateTime() {
        return joinDateTime;
    }

    public void setJoinDateTime(LocalDateTime joinDateTime) {
        this.joinDateTime = joinDateTime;
    }

    public UserProfile() {
        this.joinDateTime= LocalDateTime.now();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal  getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal  balance) {
        this.balance = balance;
    }


    public String getSmallAvatarUrl() {
        return smallAvatarUrl;
    }

    public void setSmallAvatarUrl(String smallAvatarUrl) {
        this.smallAvatarUrl = smallAvatarUrl;
    }

    public String getMediumAvatarUrl() {
        return mediumAvatarUrl;
    }

    public void setMediumAvatarUrl(String mediumAvatarUrl) {
        this.mediumAvatarUrl = mediumAvatarUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}