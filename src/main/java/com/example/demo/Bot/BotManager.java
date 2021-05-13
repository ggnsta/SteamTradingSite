package com.example.demo.Bot;

import com.example.demo.TradeOffer;
import com.example.demo.models.entity.BotDetails;
import com.example.demo.models.entity.Skins;
import com.example.demo.models.repository.BotDetailsRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;

@Component
public class BotManager {

    @Autowired
    private BotDetailsRepository bdr;
    private BotInfo botInfo;
    public static final String TIMEOFFSET = "10800"; // belarus gmt+3 = 3600s * 3
    private final String sendUrl = "https://steamcommunity.com/tradeoffer/new/send";
    private BotDetails bd=new BotDetails();//

    public void main () throws IOException {
        BotDetails botDetails = bdr.findBotDetailsBySteamLogin("qiqimc"); // ищем в бд нужного бота
        JsonObject jsonCookie = new Gson().fromJson(botDetails.getCookies(), JsonObject.class); // строку cookies из бд в json
        JsonObject jsonTransferParam =new Gson().fromJson(botDetails.getTransferParameters(), JsonObject.class);
        this.bd=botDetails;//

        this.botInfo = new BotInfo(
                botDetails.getSteamLogin(),
                botDetails.getPassword(),
                botDetails.getTradeID(),
                botDetails.getTradeToken(),
                botDetails.getSharedSecret(),
                botDetails.getIdentitySecret(),
                botDetails.getDeviceID(),
                jsonCookie,
                jsonTransferParam);

        if (jsonCookie==null) // если это первый запуск и авторизации еще не было
        {
            BotLogin botLogin = new BotLogin(botInfo);
            botLogin.prepareLogin();
            botDetails.setCookies(botLogin.getCookies().toString());
            botDetails.setTransferParameters(botLogin.getTransferParameters().toString());
            bdr.save(botDetails);
        }
       else //если куки есть, т.е. авторизация была
        {
            addCommonCookies();
            sendTradeOffer();

        }

    }


    private void addCommonCookies() {

       // botInfo.cookies.addProperty("Steam_Language","russian");
        botInfo.cookies.addProperty("timezoneOffset", TIMEOFFSET);
        botInfo.cookies.addProperty("bCompletedTradeOfferTutorial", "true");
        botInfo.cookies.addProperty("sessionid", generateSessionId());
      //  botInfo.cookies.addProperty("mobileClientVersion", "0 (2.1.3)");
      //  botInfo.cookies.addProperty("mobileClient", "android");
     //   botInfo.cookies.addProperty("dob", "");
        System.out.println(botInfo);
    }

    private HttpPost addHeaders(HttpPost httpPost, int length,String cookies)
    {
        httpPost.addHeader("Accept", "application/json; charset=UTF-8");
        httpPost.addHeader("Accept-Encoding", "gzip, deflate,br");
        httpPost.addHeader("Accept-Language", "en-US");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpPost.addHeader("Host", "steamcommunity.com");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0");
        httpPost.addHeader("Connection", "keep-alive");
       // httpPost.addHeader("Content-Length", String.valueOf(length));
        httpPost.addHeader("Cookie", cookies);
        System.out.println("1");
        return httpPost;
    }

    private static String generateSessionId() {
        return new BigInteger(96, new Random()).toString(16);
    }

    public void sendTradeOffer() throws IOException {


        List<Skins> bot = new ArrayList<>();
        Skins skin1 = new Skins();
        skin1.setAssetID("7017934365");//7017934365
        bot.add(skin1);

        JsonObject data = new JsonObject();
        data.add("sessionid",botInfo.cookies.get("sessionid")); // property?
        data.addProperty("serverid",1);
        String partnfas="76561198295609786";
        data.addProperty("partner",partnfas); // тут partnerid = steamid
        data.addProperty("tradeoffermessage","");
        JsonObject tmp = new JsonObject();
        tmp.addProperty("trade_offer_access_token","9d1xFBAE");
        data.add("trade_offer_create_params", tmp);
     //   data.add("json_tradeoffer",TradeOfferAssets.doShit(bot,null));
        String ABSDA = data.toString();



        

        String partnerTradeId = "335344058";
        String partnerToken ="9d1xFBAE";
        String referer = String.format("https://steamcommunity.com/tradeoffer/new/?partner=%s&token=%s",partnerTradeId,partnerToken);

        HttpClient httpClient = HttpClientBuilder.create().build();
        try{
            HttpPost httppost = new HttpPost(referer);
            StringEntity params = new StringEntity(data.toString());
            httppost.setEntity(params);
            httppost = addHeaders(httppost,data.size(),bd.getTradeID());
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    // do something useful
                    System.out.println("431");
                }
            }
            System.out.println("zaebumba");
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
