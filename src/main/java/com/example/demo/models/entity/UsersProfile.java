package com.example.demo.models.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.persistence.Table;

@Entity
public class UsersProfile implements UserDetails {
    @Id
    private String id;
    private String name;
    private float balance;
    private String tradeUrl;
    private String smallAvatarUrl;
    private String mediumAvatarUrl;
    private String fullAvatarUrl;
    private LocalDateTime joinDateTime;


    public String getId() {
        return id;
    }

    public String getTradeUrl() {
        return tradeUrl;
    }

    public void setTradeUrl(String tradeUrl) {
        this.tradeUrl = tradeUrl;
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

    public UsersProfile() {
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

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
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
