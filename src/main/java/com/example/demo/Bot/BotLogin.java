package com.example.demo.Bot;


import com.example.demo.Bot.Utils.RSAEncrypt;
import com.example.demo.Bot.Utils.TimeAligner;
import com.example.demo.models.entity.BotDetails;
import com.example.demo.models.repository.BotDetailsRepository;
import com.google.gson.*;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;


import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Random;



public class BotLogin {
    private JsonParser parser;
    private BotInfo botInfo;
    private JsonObject cookies;
    private JsonObject transferParameters;

    public BotLogin(BotInfo botInfo) {
        this.botInfo = botInfo;
    }

    public void prepareLogin()  {

        HttpClient ht =  HttpClient.newHttpClient();//
        JsonObject rsaKeys=getRSAKey(botInfo.steamLogin);
        String rsaPublic = rsaKeys.get("publickey_mod").getAsString();
        String rsaExp = rsaKeys.get("publickey_exp").getAsString();
        String rsaTimeStamp = rsaKeys.get("timestamp").getAsString();
        String password = new RSAEncrypt(botInfo.password, rsaPublic,rsaExp).getEncryptedPassword();
        SteamGuardAccount sga = new SteamGuardAccount(ht,botInfo);
        try {
            String twofactorcode = sga.generateSteamGuardCode();
            login(botInfo.steamLogin,password,twofactorcode,rsaTimeStamp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String formatRequestHeaders(String packet, int length)
    {
        packet += "Host: steamcommunity.com\r\n";
        packet += "User-Agent: Mozilla/5.0 (Windows NT 6.2; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0\r\n";
        packet += "Accept: application/json, text/javascript, */*r\n";
        packet += "Accept: */*\r\n";
        packet += "Accept-Language: en-US\r\n";
        packet += "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\r\n";
        packet += "Connection: close\r\n";
        packet += "Content-Length: " + length + "\r\n\r\n";
        return packet;
    }

    private JsonObject login(String login,String password, String twoFactoreCode, String rsaTimeStamp) {
        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket("steamcommunity.com", 443);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            password = URLEncoder.encode(password, "UTF-8");
            String postData = "username=" + login + "&password=" + password + "&twofactorcode="+ twoFactoreCode
                    +"&oauth_client_id="+ ""+ "&oauth_scope="+"read_profile write_profile read_client write_client"
                    + "&emailauth=&loginfriendlyname=&captchagid=-1&captcha_text=&emailsteamid=&rsatimestamp="+rsaTimeStamp + "&remember_login=true";

            String packet = "POST /login/dologin/ HTTP/1.1\r\n";
            packet+=formatRequestHeaders(packet,postData.length());

            bw.write(packet);
            bw.write(postData);
            bw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            parser = new JsonParser();
            JsonElement jsonTree = null;
            HashMap<String, String> cookiesHashMap = new HashMap<String, String>();

            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains("set-cookie: "))
                {
                    String cookieKey = line.substring(12, line.indexOf("="));
                    String cookieValue = line.substring(line.indexOf("=") + 1, line.indexOf(";"));
                    cookiesHashMap.put(cookieKey, cookieValue);
                }

                if (line.equals("")) {
                    jsonTree = parser.parse(br.readLine());
                }
            }
            JsonObject response = jsonTree.getAsJsonObject();
            bw.close();
            br.close();

            if (response.has("success")&&response.get("success").getAsBoolean()==true)
               setCookiesAndTransferParam(response,cookiesHashMap);
            else {
                throw new Exception("Login failed: "+response.toString());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private JsonObject getRSAKey(String username) {
        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket("steamcommunity.com", 443);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String postData = "username=" + username + "&donotcache=" + (System.currentTimeMillis());
            String packet = "POST /login/getrsakey/ HTTP/1.1\r\n";
            packet+=formatRequestHeaders(packet,postData.length());

            bw.write(packet);
            bw.write(postData);
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            parser = new JsonParser();
            JsonElement jsonTree = null;

            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    jsonTree = parser.parse(br.readLine());
                }
            }
            JsonObject obj = jsonTree.getAsJsonObject();
            bw.close();
            br.close();

            if (obj.has("success")&&obj.get("success").getAsBoolean()==true)
                return obj;
            else {
                throw new Exception("RSA key getting failed: "+obj.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void setCookiesAndTransferParam (JsonObject transferParam, HashMap<String, String> cookies)  {
        try {
          //  cookies.put("steamid", transferParam.getAsJsonObject("transfer_parameters").get("steamid").getAsString());
            this.cookies = new Gson().fromJson(cookies.toString(), JsonObject.class);
            this.transferParameters = transferParam.getAsJsonObject("transfer_parameters");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public JsonObject getCookies() {
        return cookies;
    }

    public JsonObject getTransferParameters() {
        return transferParameters;
    }


}