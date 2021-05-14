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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
           // addCommonCookies();
                //  sslsuka();
            konec3();

        }

    }

    private  String kakashka(Map<String, String> data) throws UnsupportedEncodingException {
        StringBuilder dataStringBuffer = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            dataStringBuffer.append(
                    URLEncoder.encode(entry.getKey(), "UTF-8"))
                    .append("=").append(
                    URLEncoder.encode(entry.getValue(), "UTF-8"))
                    .append("&");
        }
        String dataString = dataStringBuffer.toString();
        return dataString;
    }

    private void konec4()
    {
        Skins skin = new Skins();
        skin.setAssetID("7017914866");
        List<Skins> skinlist = new ArrayList<>();
        skinlist.add(skin);
        List<Skins> skinlist2 = new ArrayList<>();

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
        nameValuePairs.add(new BasicNameValuePair("sessionid", "fb3c06623858528b1a6f8637"));
        nameValuePairs.add(new BasicNameValuePair("serverid", "1"));
        nameValuePairs.add(new BasicNameValuePair("partner", "76561199073075326"));
        nameValuePairs.add(new BasicNameValuePair("tradeoffermessage", ""));
        nameValuePairs.add(new BasicNameValuePair("json_tradeoffer", TradeOfferAssets.doShit(skinlist,skinlist2).toString()));
        nameValuePairs.add(new BasicNameValuePair("captcha",""));
        nameValuePairs.add(new BasicNameValuePair("trade_offer_create_params", bd.getIdentitySecret()));

        String govno = nameValuePairs.toString();
    }
    private  void konec3() throws IOException {
        Skins skin = new Skins();
        skin.setAssetID("7017914866");
        List<Skins> skinlist = new ArrayList<>();
        skinlist.add(skin);
        List<Skins> skinlist2 = new ArrayList<>();

        Map<String, String> data = new HashMap<String, String>();
        data.put("sessionid", "fb3c06623858528b1a6f8637");
        data.put("serverid", "1");
        data.put("partner", "76561199073075326");
        data.put("tradeoffermessage", "");
        data.put("json_tradeoffer", TradeOfferAssets.doShit(skinlist,skinlist2).toString());
        data.put("trade_offer_create_params", bd.getIdentitySecret());

        String pipez = kakashka(data);
        String adres= "https://steamcommunity.com/tradeoffer/new/send";
        String refer = "https://steamcommunity.com/tradeoffer/new/?partner=&token=";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(adres);
        httpPost= addHeaders(httpPost,refer,this.bd.getTradeID());
        httpPost.setEntity(new StringEntity(pipez));

        CloseableHttpResponse response = client.execute(httpPost);
        int a =response.getStatusLine().getStatusCode();
        client.close();


    }

    private  void konec2() throws IOException {

        Skins skin = new Skins();
        skin.setAssetID("7017914866");
        List<Skins> skinlist = new ArrayList<>();
        skinlist.add(skin);
        List<Skins> skinlist2 = new ArrayList<>();

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
        nameValuePairs.add(new BasicNameValuePair("sessionid", "fb3c06623858528b1a6f8637"));
        nameValuePairs.add(new BasicNameValuePair("serverid", "1"));
        nameValuePairs.add(new BasicNameValuePair("partner", "76561199073075326"));
        nameValuePairs.add(new BasicNameValuePair("tradeoffermessage", ""));
      //  nameValuePairs.add(new BasicNameValuePair("json_tradeoffer", TradeOfferAssets.doShit(skinlist,skinlist2).toString()));
    //    nameValuePairs.add(new BasicNameValuePair("captcha",""));
    //    nameValuePairs.add(new BasicNameValuePair("trade_offer_create_params", bd.getIdentitySecret()));

        String piz1 = nameValuePairs.toString();
        piz1 = piz1.substring(1,piz1.length()-1);
        piz1=piz1.replaceAll(",","&");
        piz1=piz1.replaceAll("\\s+","");
        piz1 +="&json_tradeoffer="+URLEncoder.encode(TradeOfferAssets.doShit(skinlist,skinlist2).toString(), StandardCharsets.UTF_8);
        piz1 +="&trade_offer_create_params"+ URLEncoder.encode(bd.getIdentitySecret(),StandardCharsets.UTF_8);


        /*
        String govno = String.valueOf(nameValuePairs);
        govno = govno.substring(1,govno.length()-1);
        govno=govno.replaceAll(",","&");
        govno=govno.replaceAll("\\s+","");
        govno+= "&json_tradeoffer="+TradeOfferAssets.doShit(skinlist,skinlist2).toString()+"&";
        String dataString;
        dataString = URLEncoder.encode(govno,StandardCharsets.UTF_8);
        */


        String adres= "https://steamcommunity.com/tradeoffer/new/send";
        String refer = "https://steamcommunity.com/tradeoffer/new/?partner=&token=";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(adres);
        httpPost= addHeaders(httpPost,refer,this.bd.getTradeID());

       // httpPost.setEntity(new StringEntity(piz1));


        String postData =  "sessionid=fb3c06623858528b1a6f8637&serverid=1&partner=76561199073075326&tradeoffermessage=&json_tradeoffer=%7B%22newversion%22%3Atrue%2C%22version%22%3A1%2C%22me%22%3A%7B%22assets%22%3A%5B%7B%22appid%22%3A570%2C%22contextid%22%3A%222%22%2C%22amount%22%3A1%2C%22assetid%22%3A%227017914866%22%7D%5D%2C%22currency%22%3A%5B%5D%2C%22ready%22%3Afalse%7D%2C%22them%22%3A%7B%22assets%22%3A%5B%5D%2C%22currency%22%3A%5B%5D%2C%22ready%22%3Afalse%7D%7D&trade_offer_create_params=%7B%22trade_offer_access_token%22%3A%22Z7xgUKSi%22%7D";
        String gaylordf =  "sessionid=fb3c06623858528b1a6f8637&serverid=1&partner=76561199073075326&tradeoffermessage=&json_tradeoffer=%7B%22newversion%22%3Atrue%2C%22version%22%3A2%2C%22me%22%3A%7B%22assets%22%3A%5B%7B%22appid%22%3A%22570%22%2C%22contexid%22%3A%222%22%2C%22assetid%22%3A%227017914866%22%2C%22amount%22%3A%221%22%7D%5D%2C%22currency%22%3A%5B%5D%2C%22ready%22%3Afalse%7D%2C%22them%22%3A%7B%22assets%22%3A%5B%5D%2C%22currency%22%3A%5B%5D%2C%22ready%22%3Afalse%7D%7D&trade_offer_create_params%7B%22trade_offer_access_token%22%3A%22Z7xgUKSi%22%7D";
        httpPost.setEntity(new StringEntity(postData));

        CloseableHttpResponse response = client.execute(httpPost);
        int a =response.getStatusLine().getStatusCode();
        client.close();

    }
    private void konec() throws IOException {
        Skins skin = new Skins();
        skin.setAssetID("7017914866");
        List<Skins> skinlist = new ArrayList<>();
        skinlist.add(skin);
        List<Skins> skinlist2 = new ArrayList<>();
        String postData =  "sessionid=fb3c06623858528b1a6f8637&serverid=1&partner=76561199073075326&tradeoffermessage=&json_tradeoffer=%7B%22newversion%22%3Atrue%2C%22version%22%3A4%2C%22me%22%3A%7B%22assets%22%3A%5B%7B%22appid%22%3A570%2C%22contextid%22%3A%222%22%2C%22amount%22%3A1%2C%22assetid%22%3A%227017914866%22%7D%5D%2C%22currency%22%3A%5B%5D%2C%22ready%22%3Afalse%7D%2C%22them%22%3A%7B%22assets%22%3A%5B%5D%2C%22currency%22%3A%5B%5D%2C%22ready%22%3Afalse%7D%7D&captcha=&trade_offer_create_params=%7B%22trade_offer_access_token%22%3A%22Z7xgUKSi%22%7D";
        String adres= "https://steamcommunity.com/tradeoffer/new/send";
        String refer = "https://steamcommunity.com/tradeoffer/new/?partner=&token=";

        Map<String, String> data = new HashMap<String, String>();
        data.put("sessionid", "fb3c06623858528b1a6f8637");
        data.put("serverid", "1");
        data.put("partner", "76561199073075326");
        data.put("tradeoffermessage", "");
        data.put("json_tradeoffer", TradeOfferAssets.doShit(skinlist,skinlist2).toString());
        data.put("captcha","");
        data.put("trade_offer_create_params", bd.getIdentitySecret());

        String suka = data.toString();

        String dataString;
        dataString = URLEncoder.encode(suka,StandardCharsets.UTF_8);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(adres);
        httpPost= addHeaders(httpPost,refer,this.bd.getTradeID());

        StringEntity stringEntity = new StringEntity(dataString);
       // httpPost.setEntity(new StringEntity(postData));//вот это рабочая хуйня//2
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = client.execute(httpPost);
        int a =response.getStatusLine().getStatusCode();
        client.close();

    }

    private String mapper (Map<String,String> data) throws UnsupportedEncodingException {
        String dataString;
        StringBuilder dataStringBuffer = new StringBuilder();
        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                dataStringBuffer.append(
                        URLEncoder.encode(entry.getKey(), "UTF-8"))
                        .append("=").append(
                        URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }
        }
       return dataString = dataStringBuffer.toString();
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

    private static String generateSessionId() {
        return new BigInteger(96, new Random()).toString(16);
    }



    // рабочая хуйня
    private  void sslsuka () throws IOException {

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) factory.createSocket("steamcommunity.com", 443);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        String postData =  "sessionid=fb3c06623858528b1a6f8637&serverid=1&partner=76561199073075326&tradeoffermessage=&json_tradeoffer=%7B%22newversion%22%3Atrue%2C%22version%22%3A4%2C%22me%22%3A%7B%22assets%22%3A%5B%7B%22appid%22%3A570%2C%22contextid%22%3A%222%22%2C%22amount%22%3A1%2C%22assetid%22%3A%227017914866%22%7D%5D%2C%22currency%22%3A%5B%5D%2C%22ready%22%3Afalse%7D%2C%22them%22%3A%7B%22assets%22%3A%5B%5D%2C%22currency%22%3A%5B%5D%2C%22ready%22%3Afalse%7D%7D&captcha=&trade_offer_create_params=%7B%22trade_offer_access_token%22%3A%22Z7xgUKSi%22%7D";
        String packet = "POST /tradeoffer/new/send HTTP/1.1\r\n";
        packet+=formatRequestHeaders(packet,postData.length());

        bw.write(packet);
        bw.write(postData);
        bw.flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line;
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = null;

        while ((line = br.readLine()) != null) {

            if (line.equals("")) {
                jsonTree = parser.parse(br.readLine());
            }
        }
        JsonObject response = jsonTree.getAsJsonObject();
        bw.close();
        br.close();


    }
    private String formatRequestHeaders(String packet, int length)
    {
        packet += "Host: steamcommunity.com\r\n";
        packet += "User-Agent: Mozilla/5.0 (Windows NT 6.2; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0\r\n";
        packet += "Accept: application/json, text/javascript, */*r\n";
        packet += "Accept: */*\r\n";
        packet += "Accept-Language: en-US\r\n";
        packet += "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\r\n";
        packet += "Connection: keep-alive\r\n";
        packet += "Referer: https://steamcommunity.com/tradeoffer/new/?partner=&token=\r\n";
        packet += "Cookie:" +this.bd.getTradeID()+"\r\n";
        packet += "Content-Length: " + length + "\r\n\r\n";
        return packet;
    }
}

