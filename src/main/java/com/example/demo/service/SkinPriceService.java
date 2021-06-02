package com.example.demo.service;

import com.example.demo.models.entity.SkinPrice;

import com.example.demo.models.repository.SkinPriceRepository;
import com.example.demo.utls.HTTPClientGame;
import com.example.demo.utls.MyJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;

import java.util.List;


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
        }catch (Exception ex){ ex.printStackTrace();}
        MyJsonParser parser = new MyJsonParser();
        if(response!=null)
        {
        priceInfo=parser.parsePriceOwerview(response);

            SkinPrice skinPrice = new SkinPrice();
            skinPrice.setMarketHashName(marketHashName);
            skinPrice.setLowestPrice(Double.parseDouble(priceInfo.get(0).substring(1)));
            skinPrice.setMedianPrice(Double.parseDouble(priceInfo.get(0).substring(1)));

            BigDecimal result = new BigDecimal(skinPrice.getLowestPrice()*0.7);
            result=result.setScale(3, RoundingMode.DOWN);
            skinPrice.setPurchasePrice(result.doubleValue());

            result = new BigDecimal(skinPrice.getLowestPrice()*0.8);
            result=result.setScale(3, RoundingMode.DOWN);


            skinPrice.setSellingPrice(result.doubleValue());
            skinPrice.setCurrency(priceInfo.get(0).substring(0,1));
            skinPriceRepository.save(skinPrice);

            return skinPrice;
        }
        else {
            SkinPrice skinPrice = new SkinPrice();
            skinPrice.setMarketHashName(marketHashName);
            skinPriceRepository.save(skinPrice);
            return skinPrice;

        }

    }
}
