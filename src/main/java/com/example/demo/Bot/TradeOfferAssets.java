package com.example.demo.Bot;

import com.example.demo.models.entity.Skins;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TradeOfferAssets {
    private class CEconTradeStatus {

        @SerializedName("newversion")
        private boolean newVersion;

        @SerializedName("version")
        private long version;

        @SerializedName("me")
        private CEconTradePartipiant me;

        @SerializedName("them")
        private CEconTradePartipiant them;

    }

    private class CEconTradePartipiant {

        @SerializedName("ready")
        private boolean ready;

        @SerializedName("assets")
        private List<Skins> assets;

    }

    private static class OnlySkinsAssets
    {
        private String appid;
        private String contextid;
        private String ammount;
        private String assetid;

        public OnlySkinsAssets(String appid, String contextid, String ammount, String assetid) {
            this.appid = appid;
            this.contextid = contextid;
            this.ammount = ammount;
            this.assetid = assetid;
        }
    }

    public   CEconTradeStatus  doShit (List<Skins> bot,List<Skins> user)
    {

       // JsonObject jsonTradeOffer = new JsonObject();
        //jsonTradeOffer.addProperty("newversion" ,true);
       // jsonTradeOffer.addProperty("version",1);
       // jsonTradeOffer.add("me",makePartOfAssets(bot));
      //  jsonTradeOffer.add("them",makePartOfAssets(user));
        //jsonTradeOffer.add("me",dogoodShit(bot));
      //  jsonTradeOffer.add("them",dogoodShit(user));
        CEconTradePartipiant me = new CEconTradePartipiant();
        me.ready = false;
        me.assets = bot;
        CEconTradePartipiant partner = new CEconTradePartipiant();
        partner.ready = false;
        partner.assets = user;

        CEconTradeStatus tradeStatus = new CEconTradeStatus();
        tradeStatus.newVersion = true;
        tradeStatus.version = 1;
        tradeStatus.me = me;
        tradeStatus.them = partner;


        return tradeStatus;
    }

    public static JsonObject dogoodShit(List<Skins> skin)
    {
        if (skin!=null) {

            JsonObject user = new JsonObject();
            user.addProperty("ready", false);
            user.add("currency", new JsonArray());
            JsonArray array = new JsonArray();
            for (int i = 0; i < skin.size(); i++) {
                JsonObject obj = new JsonObject();
                obj.addProperty("appid", "530");
                obj.addProperty("contexid", "2");
                obj.addProperty("assetid", skin.get(i).getAssetID());
                obj.addProperty("amount", "1");
                array.add(obj);
            }
            user.add("assets", array);
            return user;
        }else return null;
    }

    private static JsonObject makePartOfAssets(List<Skins> skin)
    {
        if (skin!=null) {
            List<OnlySkinsAssets> assets = new ArrayList<>();
            for (int i = 0; i < skin.size(); i++) {
                assets.add(new OnlySkinsAssets("530", "2", "1", skin.get(i).getAssetID()));
            }
            JsonElement elementAseets = new Gson().toJsonTree(assets);
            JsonObject objAssets = new JsonObject();
            objAssets.add("assets", elementAseets);
            objAssets.add("currency", new JsonArray()); // неиспользуемый, но необходимый параметр, должен быть массивом[]
            objAssets.addProperty("ready", "false");
            return objAssets;
        }
        else
            return null;
    }


}
