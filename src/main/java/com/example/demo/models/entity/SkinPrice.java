package com.example.demo.models.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity

public class SkinPrice implements Serializable {

    @Id
    @Column (name="market_hash_name")
    private String marketHashName;
    private Double medianPrice;
    private Double lowestPrice;
    private String currency;
    @OneToMany(targetEntity=Skins.class, mappedBy="skinPrice",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Skins> skins = new ArrayList<>();

    public void addSkins(Skins skin){
        skins.add(skin);
        skin.setSkinPrice(this);
    }

    public void removeSkins(Skins skin){
        skins.remove(skin);
        skin.setSkinPrice(null);
    }

    public String getMarketHashName() {
        return marketHashName;
    }

    public void setMarketHashName(String marketHashName) {
        this.marketHashName = marketHashName;
    }

    public List<Skins> getSkin() {
        return skins;
    }

    public void setSkin(List<Skins> skin) {
        this.skins = skin;
    }

    public Double getMedianPrice() {
        return medianPrice;
    }

    public void setMedianPrice(Double medianPrice) {
        this.medianPrice = medianPrice;
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}