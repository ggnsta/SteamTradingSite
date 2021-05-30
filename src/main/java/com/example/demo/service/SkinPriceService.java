package com.example.demo.service;

import com.example.demo.models.entity.SkinPrice;

import com.example.demo.models.repository.SkinPriceRepository;
import com.example.demo.utls.HTTPClientGame;
import com.example.demo.utls.MyJsonParser;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.ArrayList;

import java.util.List;


@Service
public class SkinPriceService {

    @Autowired
    private SkinPriceRepository skinPriceRepository;
    private static final  String marketURL = "https://steamcommunity.com/market/priceoverview/?appid=%appId&currency=%currency&market_hash_name=%marketHashName";

    public SkinPrice fake ()
    {
        SkinPrice sp = new SkinPrice();
        skinPriceRepository.save(sp);
        return sp;
    }

    public void test2()
    {
        for(int i = 0; i<150;i++)
        {
            HTTPClientGame  httpClientGame = new HTTPClientGame("https://steamcommunity.com/market/listings/730/Snakebite%20Case");
            String response = httpClientGame.getAll();
            System.out.println(i);
        }

    }

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
