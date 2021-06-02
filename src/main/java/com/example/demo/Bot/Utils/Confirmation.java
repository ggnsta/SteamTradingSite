package com.example.demo.Bot.Utils;

public class Confirmation {

    private String  ID;
    private String Key;
    private String IntType;
    private String Creator;
    private ConfirmationType ConfType;

    public Confirmation(String id, String key, String type, String creator) {
        this.ID = id;
        this.Key = key;
        this.IntType = type;
        this.Creator = creator;

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