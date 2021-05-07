package com.example.demo.models.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Skins {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    String id;
    String ownerID;
    String assetID;
    String classID;
    String instanceID;
    @Column(length = 300)
    String iconUrl;
    int tradable;
    String marketHashName;
    int marketable;
    String quality;

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skins skins = (Skins) o;
        return Objects.equals(assetID, skins.assetID) &&
                Objects.equals(classID, skins.classID) &&
                Objects.equals(instanceID, skins.instanceID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assetID, classID, instanceID);
    }

    public String getAssetID() {
        return assetID;
    }

    public void setAssetID(String assetID) {
        this.assetID = assetID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = "https://community.cloudflare.steamstatic.com/economy/image/"+iconUrl;
    }

    public int getTradable() {
        return tradable;
    }

    public void setTradable(int tradable) {
        this.tradable = tradable;
    }

    public String getMarketHashName() {
        return marketHashName;
    }

    public void setMarketHashName(String marketHashName) {
        this.marketHashName = marketHashName;
    }

    public int getMarketable() {
        return marketable;
    }

    public void setMarketable(int marketable) {
        this.marketable = marketable;
    }
    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

}
