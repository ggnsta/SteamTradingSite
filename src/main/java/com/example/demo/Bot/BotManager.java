package com.example.demo.Bot;

import com.example.demo.models.entity.BotDetails;
import com.example.demo.models.entity.Skins;
import com.example.demo.models.entity.TradeOffer;
import com.example.demo.models.entity.UserProfile;
import com.example.demo.models.repository.BotDetailsRepository;
import com.example.demo.models.repository.UserProfileRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.*;

@Component
public class BotManager {

    @Autowired
    private BotDetailsRepository botDetailsRepositorydr;
    @Autowired
    private UserProfileRepository userProfileRepository;
    private BotInfo botInfo;
    public static final String TIMEOFFSET = "10800"; // belarus gmt+3 = 3600s * 3
    private final String sendUrl = "https://steamcommunity.com/tradeoffer/new/send";
    private BotDetails bd=new BotDetails();//
    private  String sessionID;

    public void main () throws IOException {
        BotDetails botDetails = botDetailsRepositorydr.findBotDetailsBySteamLogin("qiqimc"); // ищем в бд нужного бота;
        this.bd=botDetails;//

        this.botInfo = new BotInfo(
                botDetails.getSteamLogin(),
                botDetails.getPassword(),
                botDetails.getTradeID(),
                botDetails.getTradeToken(),
                botDetails.getSharedSecret(),
                botDetails.getIdentitySecret(),
                botDetails.getDeviceID(),
                botDetails.getCookies());

        if (botInfo.cookies.equals("")) // если это первый запуск и авторизации еще не было
        {
            BotLogin botLogin = new BotLogin(botInfo);
            botLogin.prepareLogin();
            botDetails.setCookies(botLogin.getCookies().toString());
           // botDetails.setTransferParameters(botLogin.getTransferParameters().toString());
            botDetailsRepositorydr.save(botDetails);
        }
       else //если куки есть, т.е. авторизация была
        {
            addCommonCookies();
            Skins skin = new Skins();
            skin.setAssetID("7017914866");
            List<Skins> skinlist = new ArrayList<>();
            skinlist.add(skin);
            List<Skins> skinlist2 = new ArrayList<>();

          //  sendTradeOffer(skinlist,skinlist2,"76561198401441827","");

            cancelTradeOffer("4607810784");
        }

    }

    private static String generateSessionId() {
        return new BigInteger(96, new Random()).toString(16);
    }

    private static String generateTradeOfferMessage()
    {
        String tradeCode = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        return  tradeCode;
    }

    private  void sendTradeOffer(List<Skins> botSkins, List <Skins> userSkins, String partnerSteamID,String userTradeOfferAccessToken) throws IOException {

        Map<String, String> data = new HashMap<String, String>();
        data.put("sessionid", this.sessionID);
        data.put("serverid", "1");
        data.put("partner", partnerSteamID);
        data.put("tradeoffermessage", "");
        data.put("json_tradeoffer", TradeOfferAssets.tradableSkinsToJson(botSkins,userSkins).toString());
        data.put("trade_offer_create_params", userTradeOfferAccessToken);
        String requestBody = encodeRequestBody(data);

        String requestAdress= "https://steamcommunity.com/tradeoffer/new/send";
        String refer = "https://steamcommunity.com/tradeoffer/new/?partner=&token=";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(requestAdress);
        httpPost= addHeaders(httpPost,refer,botInfo.getCookies());
        httpPost.setEntity(new StringEntity(requestBody));

        CloseableHttpResponse response = client.execute(httpPost);
        int a =response.getStatusLine().getStatusCode();
        HttpEntity entity=response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        System.out.println(responseString);
        client.close();
    }

    public void cancelTradeOffer(String tradeOfferID) throws IOException {
        String url = "https://steamcommunity.com/tradeoffer/" + tradeOfferID+ "/cancel";
        Map<String, String> data = new HashMap<String, String>();
        data.put("sessionid", this.sessionID);
        String requestBody = encodeRequestBody(data);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost= addheaders2(httpPost);
        httpPost.setEntity(new StringEntity(requestBody));

        CloseableHttpResponse response = client.execute(httpPost);
        int a =response.getStatusLine().getStatusCode();
        HttpEntity entity=response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        System.out.println(responseString);
        client.close();


    }

    private void createTradeOfferObj(String tradeOfferID, String botLogin, String partnerID)
    {
        TradeOffer tradeOffer = new TradeOffer();
        tradeOffer.setId(tradeOfferID);
        tradeOffer.setBotLogin(botDetailsRepositorydr.findBotDetailsBySteamLogin(botLogin));
        tradeOffer.setUserProfileID(userProfileRepository.findById(partnerID).get());
        tradeOffer.setMessage(generateTradeOfferMessage());
    }

    private void addCommonCookies() {
        this.sessionID = generateSessionId();
        String cookies = botInfo.getCookies();
        StringBuffer strBuffer = new StringBuffer(cookies);
        strBuffer.append(";timezoneOffset="+TIMEOFFSET);
        strBuffer.append(";bCompletedTradeOfferTutorial="+"true");
        strBuffer.append(";sessionid="+this.sessionID);
        botInfo.setCookies(strBuffer.toString());
        System.out.println(botInfo);
    }

    private HttpPost addHeaders(HttpPost httpPost, String referer,String cookies)
    {
        httpPost.addHeader("Accept", "application/json; charset=UTF-8");
        httpPost.addHeader("Accept-Encoding", "gzip, deflate,br");
        httpPost.addHeader("Accept-Language", "en-US");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpPost.addHeader("Host", "steamcommunity.com");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0");
        httpPost.addHeader("Connection", "keep-alive");
        httpPost.addHeader("Cookie", cookies);
        httpPost.addHeader("Referer", referer);
        return httpPost;
    }

    private HttpPost addheaders2(HttpPost httpPost)
    {
        httpPost.addHeader("Accept", "*/*");
        httpPost.addHeader("Cookie",botInfo.getCookies());
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        return httpPost;
    }
    private  String encodeRequestBody(Map<String, String> data) {
        StringBuilder dataStringBuffer = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                dataStringBuffer.append(
                        URLEncoder.encode(entry.getKey(), "UTF-8"))
                        .append("=").append(
                        URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        String dataString = dataStringBuffer.toString();
        return dataString;
    }
}

