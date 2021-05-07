package com.example.demo.service;
import com.example.demo.GeneralAPI;
import com.example.demo.models.entity.Skins;
import com.example.demo.models.entity.UsersProfile;
import com.example.demo.models.repository.SkinsRepository;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class InventoryService {
    private static final String url = "https://steamcommunity.com/inventory/%username/730/2";
    private static final JsonParser jsonParser = new JsonParser();
    private final SkinsRepository skinsRepository;
    //QUALITIES
    private final int FACTORY_NEW = 1;
    private final int MINIMAL_WEAR = 2;
    private final int FIELD_TESTED = 3;
    private final int WELL_WORN = 4;
    private final int BATTLE_SCARED = 5;

    private String[] QUALITIES = new String[]{
            "(Factory New)",
            "(Minimal Wear)",
            "(Field-Tested)",
            "(Well-Worn)",
            "(Battle-Scarred)"
    };
    @Autowired
    public InventoryService(SkinsRepository skinsRepository) {
        this.skinsRepository = skinsRepository;
    }

    public JsonObject requestInventoryJsonObject(String steamID) throws IOException, InterruptedException {
        JsonObject jsonObject = null;
        try {
            URL urlObject = new URL(url.replace("%username", steamID));
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlObject.openConnection();
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println("RC: " + responseCode);
            if (responseCode == 429) {
                GeneralAPI.currentLog = "[Error] Your app has made too many requests.";
                return null;
            } else if (responseCode == 400) {
                GeneralAPI.currentLog = "[Error] Bad Request.";
                return null;
            }
            Scanner scanner = new Scanner(urlObject.openStream());
            StringBuilder json = null;
            while (scanner.hasNextLine()) {
                if (json != null) {
                    json.append(scanner.nextLine());
                } else {
                    json = new StringBuilder(scanner.nextLine());
                }
            }

            if (json != null) {
                jsonObject = (JsonObject) jsonParser.parse(json.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public List<Skins> mappJsonSkinArray(UsersProfile user)
    {
        try {
            String steamID = user.getId();
            String steamIdFull = steamID;
            steamID = steamID.substring(37);

            JsonObject skinsJsonObject = requestInventoryJsonObject(steamID);
            JsonArray skinDesctiptionJson = skinsJsonObject.getAsJsonArray("descriptions");
            JsonArray skinAssetJson = skinsJsonObject.getAsJsonArray("assets");

            List <Skins> skinsToDB= new ArrayList<>();
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
                userSkin.setMarketable(skinDesctiptionJson.get(k).getAsJsonObject().get("marketable").getAsInt());
                userSkin.setQuality(defineSkinQuality(userSkin.getMarketHashName()));
                userSkin.setOwnerID(steamIdFull);
                skinsToDB.add(userSkin);
            }
            return skinsToDB;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void updateUserSkinsDatabase(UsersProfile user)
    {
        List<Skins> existSkins = getUserSkins(user); // скины, которые уже в бд
        List<Skins> actualSkins = mappJsonSkinArray(user);//только что полученные скины от steam

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

    private List<Skins> getUserSkins (UsersProfile user)
    {
        return skinsRepository.findAllByOwnerID(user.getId());
    }

    public int searchJsonDescriptionForSkin(JsonArray array, String searchValue){

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
}

