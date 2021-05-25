package com.example.demo.models.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity

public class SkinPrice {

    @Id
    @Column (name="market_hash_name")
    private String marketHashName;
    private Double medianPrice;
    private Double lowestPrice;
    private String currency;
   // @OneToMany(targetEntity=Skins.class, mappedBy="skinPrice",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
  //  private List<Skins> skin = new ArrayList<>();

    public String getMarketHashName() {
        return marketHashName;
    }

    public void setMarketHashName(String marketHashName) {
        this.marketHashName = marketHashName;
    }
/*
    public List<Skins> getSkin() {
        return skin;
    }

    public void setSkin(List<Skins> skin) {
        this.skin = skin;
    }
*/
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