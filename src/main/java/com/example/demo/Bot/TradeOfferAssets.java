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


    public static JsonObject  tradableSkinsToJson (List<Skins> bot,List<Skins> user)
    {
        JsonObject jsonTradeOffer = new JsonObject();
        jsonTradeOffer.addProperty("newversion" ,true);
        jsonTradeOffer.addProperty("version",2);
        jsonTradeOffer.add("me",makePartOfAssets(bot));
        jsonTradeOffer.add("them",makePartOfAssets(user));

        return jsonTradeOffer;
    }

    private static JsonObject makePartOfAssets(List<Skins> skin)
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

}
