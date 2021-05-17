package com.example.demo.service;

import com.example.demo.models.entity.SkinPrice;
import com.example.demo.models.entity.Skins;
import com.example.demo.models.repository.SkinPriceRepository;
import com.example.demo.utls.HTTPClientGame;
import com.example.demo.utls.MyJsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkinPriceService {

    @Autowired
    private SkinPriceRepository skinPriceRepository;
    private static final  String marketURL = "https://steamcommunity.com/market/priceoverview/?appid=%appId&currency=%currency&market_hash_name=%marketHashName";


    public SkinPrice requestOneSkinPrice (int currency, String marketHashName)
    {
        List<String> priceInfo = new ArrayList<>();
        String response=null;
        try {
            String encodedHashName = URLEncoder.encode(marketHashName,"UTF-8");
            String url = marketURL.replace("%marketHashName", encodedHashName).replace("%appId", String.valueOf(730)).replace("%currency", String.valueOf(currency));
            HTTPClientGame httpClientGame = new HTTPClientGame(url);
            response = httpClientGame.getAll();
        }catch (Exception ex){
            //вот тут что-то помягче
            ex.printStackTrace();
        }
        MyJsonParser parser = new MyJsonParser();
        priceInfo=parser.parsePriceOwerview(response);
        if(priceInfo!=null)
        {
            SkinPrice skinPrice = new SkinPrice();
            skinPrice.setMarketHashName(marketHashName);
            skinPrice.setLowestPrice(Double.parseDouble(priceInfo.get(0).substring(1)));
            skinPrice.setMedianPrice(Double.parseDouble(priceInfo.get(0).substring(1)));
            skinPrice.setCurrency(priceInfo.get(0).substring(0,1));
            skinPriceRepository.save(skinPrice);

            return skinPrice;

        }
        else return null;

    }
}
