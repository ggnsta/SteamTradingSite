package com.example.demo.service;
import com.example.demo.models.entity.SkinPrice;
import com.example.demo.models.entity.Skins;
import com.example.demo.models.entity.UserProfile;
import com.example.demo.models.repository.SkinPriceRepository;
import com.example.demo.models.repository.SkinsRepository;
import com.example.demo.utls.HTTPClientGame;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    private static final String url = "https://steamcommunity.com/inventory/%username/730/2";
    private static final JsonParser jsonParser = new JsonParser();
    private SkinsRepository skinsRepository;

    private String[] QUALITIES = new String[]{
            "(Factory New)",
            "(Minimal Wear)",
            "(Field-Tested)",
            "(Well-Worn)",
            "(Battle-Scarred)"
    };
    @Autowired
    private SkinPriceService skinPriceService;
    @Autowired
    public InventoryService(SkinsRepository skinsRepository) {
        this.skinsRepository = skinsRepository;
    }
    @Autowired
    private SkinPriceRepository skinPriceRepository;

    private JsonObject requestInventory(String steamID)
    {
        String reqUrl = url.replace("%username", steamID);
        HTTPClientGame httpClientGame = new HTTPClientGame(reqUrl);
        String response = httpClientGame.getAll();
        if (response!=null) {
            JsonObject jsonObject = (JsonObject) jsonParser.parse(response.toString());
            return jsonObject;
        }
        else throw new NullPointerException();
    }

    public List<Skins> parseJsonToSkins(UserProfile user)
    {
        JsonObject skinsJsonObject = requestInventory(user.getId());
        JsonArray skinDesctiptionJson = skinsJsonObject.getAsJsonArray("descriptions");
        JsonArray skinAssetJson = skinsJsonObject.getAsJsonArray("assets");
        List <Skins> skinsToDB= new ArrayList<>();
        for (int i = 0; i<skinDesctiptionJson.size();i++)
        {
            String marketHashName = skinDesctiptionJson.get(i).getAsJsonObject().get("market_hash_name").getAsString();
            skinPriceService.requestOneSkinPrice(1,marketHashName);
        }
        for (int i = 0; i<skinAssetJson.size();i++)
        {
            Skins userSkin= new Skins();
            userSkin.setAssetID(skinAssetJson.get(i).getAsJsonObject().get("assetid").getAsString());
            userSkin.setClassID(skinAssetJson.get(i).getAsJsonObject().get("classid").getAsString());
            userSkin.setInstanceID(skinAssetJson.get(i).getAsJsonObject().get("instanceid").getAsString());
            int k = searchJsonDescriptionForSkin(skinDesctiptionJson,skinAssetJson.get(i).getAsJsonObject().get("classid").getAsString());
            userSkin.setIconUrl(skinDesctiptionJson.get(k).getAsJsonObject().get("icon_url").getAsString());
            userSkin.setMarketHashName(skinDesctiptionJson.get(k).getAsJsonObject().get("market_hash_name").getAsString());
            userSkin.setTradable(skinDesctiptionJson.get(k).getAsJsonObject().get("tradable").getAsInt());
            if(userSkin.getTradable()==0)continue;// если предмететом нельзя обмениваться, в бд он не вносится
            userSkin.setMarketable(skinDesctiptionJson.get(k).getAsJsonObject().get("marketable").getAsInt());
            userSkin.setQuality(defineSkinQuality(userSkin.getMarketHashName()));
            userSkin.setUserProfile(user);


            Optional<SkinPrice>  skinPriceOptional = skinPriceRepository.findById(userSkin.getMarketHashName());
            SkinPrice skinPrice = skinPriceOptional.orElse(skinPriceOptional.get());

            userSkin.setSkinPrice(skinPrice);
            skinPrice.addSkins(userSkin);
            skinsRepository.save(userSkin);


            //skinsToDB.add(userSkin);
        }
        return skinsToDB;
    }

    //to-do цены обновляются только на скины которые перезаписались в дб, сделать обновление цен для старых скинов
    public void updateUserSkinsDatabase(UserProfile user)
    {
        List<Skins> existSkins = getUserSkins(user); // скины, которые уже в бд
        List<Skins> actualSkins= parseJsonToSkins(user);

        if(!existSkins.containsAll(actualSkins)|| !actualSkins.containsAll(existSkins))
        {
            List<Skins> skinsToDeleteFromDB = existSkins; // скины которые надо удалить из дб
            skinsToDeleteFromDB.removeAll(actualSkins);// из этого массива удаляем совпадения с актуальынми скинами, остаются только те которых уже нет в стиме
            skinsRepository.deleteAll(skinsToDeleteFromDB);// удаляем несуществующие скины из нашей бд
            existSkins=getUserSkins(user);
            for(Skins skin : actualSkins)
            {
                if(existSkins.contains(skin)) {
                    System.out.println();
                }
                else {
                    skinsRepository.save(skin);

                }
            }
        }
    }

    private String defineSkinQuality(String hashName)
    {
        for (int quality = 1; quality <= QUALITIES.length; quality++) {
            if (hashName.contains(QUALITIES[quality - 1]))
                return QUALITIES[quality-1];
        }
        return null;
    }

    private List<Skins> getUserSkins (UserProfile user)
    {
        List<Skins> skinsList = user.getSkins();
        return skinsList;
    }

    private int searchJsonDescriptionForSkin(JsonArray array, String searchValue){

        for (int i = 0; i < array.size(); i++) {
            JsonObject obj= null;
            try {
                obj = array.get(i).getAsJsonObject();
                if(obj.get("classid").getAsString().equals(searchValue))
                {
                    return i;
                }
            } catch (JsonIOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public String calculateInventoryCost (List<Skins> userSkins)
    {
        double cost=0;
        for(int i =0; i<userSkins.size();i++)
        {
            if (userSkins.get(i).getSkinPrice()!=null)
            cost+= userSkins.get(i).getSkinPrice().getLowestPrice();
        }
        return  String.valueOf(cost);
    }

}