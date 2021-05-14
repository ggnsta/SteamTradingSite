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

    public static JsonObject  doShit (List<Skins> bot,List<Skins> user)
    {

        JsonObject jsonTradeOffer = new JsonObject();
        jsonTradeOffer.addProperty("newversion" ,true);
        jsonTradeOffer.addProperty("version",2);
        jsonTradeOffer.add("me",dogoodShit(bot));
        jsonTradeOffer.add("them",dogoodShit(user));



        return jsonTradeOffer;
    }

    public static JsonObject dogoodShit(List<Skins> skin)
    {
        if (skin!=null) {

            JsonObject user = new JsonObject();
            JsonArray array = new JsonArray();
            for (int i = 0; i < skin.size(); i++) {
                JsonObject obj = new JsonObject();
                obj.addProperty("appid", 570);
                obj.addProperty("contextid", "2");
                obj.addProperty("amount", "1");
                obj.addProperty("assetid", skin.get(i).getAssetID());
                array.add(obj);
            }
            user.add("assets", array);
            user.add("currency", new JsonArray());
            user.addProperty("ready", false);

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
