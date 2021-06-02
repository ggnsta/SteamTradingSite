package com.example.demo.utls;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;

public class MyJsonParser {

    private JsonParser parser=new JsonParser();

    public   List<String> parsePriceOwerview(String stringToParse)
    {
        JsonElement jsonTree = parser.parse(stringToParse);
        JsonObject obj = jsonTree.getAsJsonObject();
        List<String> priceOwerview = new ArrayList<>();

        if(obj.get("success").getAsString()=="true")
        {
            priceOwerview.add(obj.get("lowest_price").getAsString());
            priceOwerview.add(obj.get("median_price").getAsString());
            return priceOwerview;
        }
        else return null;
    }
    public List<String> parseUserInfoList(String stringToParse) {

        JsonElement jsonTree = parser.parse(stringToParse);
        JsonObject obj = jsonTree.getAsJsonObject();
        obj = obj.getAsJsonObject("response");
        JsonArray info = obj.getAsJsonArray("players");
        obj = info.get(0).getAsJsonObject();

        List<String> userInfo = new ArrayList<>();
        userInfo.add(obj.get("personaname").getAsString());
        userInfo.add(obj.get("avatar").getAsString());
        userInfo.add(obj.get("avatarmedium").getAsString());
        userInfo.add(obj.get("avatarfull").getAsString());

        return userInfo;
    }

    public static JsonObject  parseTimeAligner (String stringToParse)
    {
        JsonParser parser= new JsonParser();
        JsonElement jsonTree = parser.parse(stringToParse);
        JsonObject obj = jsonTree.getAsJsonObject();
        obj = obj.getAsJsonObject("response");
        return obj;
    }
}
