package com.example.demo;

import com.example.demo.models.entity.Skins;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeBot  {

    public int trade_version;





    public int makeTradeoffer (String steamId, List<Skins> botSkins, List<Skins> userSkin, String message) {

        JSONObject tradeStatus = new JSONObject();
        tradeStatus.put("newversion", true);
        tradeStatus.put("version", trade_version);

        JSONObject userMe = new JSONObject();
        userMe.put("ready", false);
        userMe.put("currency", new JSONArray());
        JSONArray assets = new JSONArray();
        for (Skins skin : botSkins )
        {
            JSONObject jsonAsset = new JSONObject();
            jsonAsset.put("appid","730");
            jsonAsset.put("contextid", "2");
            jsonAsset.put("assetid", skin.getAssetID());
            jsonAsset.put("ammount","1");
            assets.add(jsonAsset);
        }
        userMe.put("asstes",assets);
        tradeStatus.put("me",userMe);



        return 0;
    }
}
