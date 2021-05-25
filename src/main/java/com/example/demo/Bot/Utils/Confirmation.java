package com.example.demo.Bot.Utils;

public class Confirmation {

    private String  ID;//The ID of this confirmation
    private String Key;//The unique key used to act upon this confirmation.
    private String IntType;// The value of the data-type HTML attribute returned for this contribution.
    private String Creator;// Represents either the Trade Offer ID or market transaction ID that caused this confirmation to be created.
    private ConfirmationType ConfType;// The type of this confirmation.

    public Confirmation(String id, String key, String type, String creator) {
        this.ID = id;
        this.Key = key;
        this.IntType = type;
        this.Creator = creator;

        //Do a switch simply because we're not 100% certain of all the possible types.
        switch (Integer.parseInt(type)) {
            case 1:
                this.ConfType = ConfirmationType.GenericConfirmation;
                break;
            case 2:
                this.ConfType = ConfirmationType.Trade;
                break;
            case 3:
                this.ConfType = ConfirmationType.MarketSellTransaction;
                break;
            default:
                this.ConfType = ConfirmationType.Unknown;
                break;
        }
    }

    @Override
    public String toString() {
        return "Confirmation{" +
                "ID='" + ID + '\'' +
                ", Key='" + Key + '\'' +
                ", IntType='" + IntType + '\'' +
                ", Creator='" + Creator + '\'' +
                ", ConfType=" + ConfType +
                '}';
    }

    public enum ConfirmationType {
        GenericConfirmation,
        Trade,
        MarketSellTransaction,
        Unknown
    }

    public String getID() {
        return ID;
    }

    public String getKey() {
        return Key;
    }

    public String getIntType() {
        return IntType;
    }

    public String getCreator() {
        return Creator;
    }

    public ConfirmationType getConfType() {
        return ConfType;
    }
}