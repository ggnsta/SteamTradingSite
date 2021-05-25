package com.example.demo.Bot;

import com.example.demo.Bot.Utils.Confirmation;
import com.example.demo.Bot.Utils.HmacSHA1Handler;
import com.example.demo.Bot.Utils.TimeAligner;
import com.example.demo.utls.EndPoints;
import com.example.demo.utls.HTTPClientGame;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SteamGuardAccount {


    private HttpClient client; // убрать может
    private final static byte[] steamGuardCodeTranslations = new byte[] { 50, 51, 52, 53, 54, 55, 56, 57, 66, 67, 68, 70, 71, 72, 74, 75, 77, 78, 80, 81, 82, 84, 86, 87, 88, 89 };
    private BotInfo botInfo;


    /**
     * SteamGuardAccount to get the steamGuard code for login, accept send step of 2 step confirmation.
     * @param client HttpClient
     * @param botInfo secrets for 2fa, cookies....
     */
    public SteamGuardAccount(HttpClient client, BotInfo botInfo) {
        this.client = client;
        this.botInfo = botInfo;
    }

    /**
     * Generate Steam Guard Code required for login.
     * @return Steam Guard Code
     * @throws IOException
     * @throws InterruptedException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public String generateSteamGuardCode()
            throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeyException {
        return generateSteamGuardCodeForTime(TimeAligner.getSteamTime(client));
    }

    /**
     * Generate Steam Guard Code for a given time.
     * @param time Epoch Time/Unix Time
     * @return Steam Guard code
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public String generateSteamGuardCodeForTime(long time)
            throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        if (this.botInfo.sharedSecret == null || this.botInfo.sharedSecret.length() == 0) {
            return null;
        }

        byte[] sharedSecretArray = Base64.getDecoder().decode(this.botInfo.sharedSecret);

        byte[] timeArray = new byte[8];

        time /= 30L;

        for (int i = 8; i > 0; i--) {
            timeArray[i - 1] = (byte)time;
            time >>= 8;
        }

        byte[] hashedData = HmacSHA1Handler.calculateHMACSHA1(timeArray, sharedSecretArray);

        byte[] codeArray = new byte[5];
        try {
            byte b = (byte)(hashedData[19] & 0xF);
            int codePoint = (hashedData[b] & 0x7F) << 24 | (hashedData[b + 1] & 0xFF) << 16 | (hashedData[b + 2] & 0xFF) << 8 | (hashedData[b + 3] & 0xFF);

            for (int i = 0; i < 5; ++i) {
                codeArray[i] = steamGuardCodeTranslations[codePoint % steamGuardCodeTranslations.length];
                codePoint /= steamGuardCodeTranslations.length;
            }
        }
        catch (Exception e) {
            return null;
        }
        return new String(codeArray, StandardCharsets.UTF_8);
    }


    public List<Confirmation> fetchConfirmations()
    {
        String url = generateConfirmationURL("conf");


        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", botInfo.getCookies());
        headers.put("Referer", EndPoints.COMMUNITY_BASE);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.1.1; en-us; Google Nexus 4 - 4.1.1 - API 16 - 768x1280 Build/JRO03S) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        headers.put("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
        headers.put("Origin", "https://steamcommunity.com");


        HttpRequest request = HTTPClientGame.build(url, headers, HTTPClientGame.RequestType.POST, "");
        HttpResponse<String> response=null;
        try {
           response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        if(response.statusCode() == 200) {
          System.out.println("Confirmation Request Fetched Successfully.");
        }

        return null;

    }
    private String generateConfirmationURL(String tag)
    {
        String endpoint = EndPoints.COMMUNITY_BASE + "/mobileconf/conf?";
        String queryString = generateConfirmationQueryParams(tag);
        return endpoint + queryString;
    }

    private String generateConfirmationQueryParams(String tag)
    {
        if(Objects.equals(botInfo.deviceID, null))
            throw new IllegalArgumentException("Device ID is not present(null)");

        Map<String, String> queryParams = generateConfirmationQueryParamsAsNVC(tag);

        return "p=" + queryParams.get("p")
                + "&a=" + queryParams.get("a")
                + "&k=" + queryParams.get("k")
                + "&t=" + queryParams.get("t")
                + "&m=android&tag=" + queryParams.get("tag");
    }
    private Map<String, String> generateConfirmationQueryParamsAsNVC(String tag) {
        try {
            if (Objects.equals(botInfo.deviceID, null))
                throw new IllegalArgumentException("Device ID is not present(null)");

            Long time = TimeAligner.getSteamTime(client);

            Map<String, String> ret = new HashMap<>();
            ret.put("p", botInfo.deviceID);
            ret.put("a", "76561199129663400"); //----Check
            ret.put("k", generateConfirmationHashForTime(time, tag));
            ret.put("t", time.toString());
            ret.put("m", "android");
            ret.put("tag", tag);

            return ret;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    private String generateConfirmationHashForTime(long time, String tag)
            throws UnsupportedEncodingException {

        byte[] decode = Base64.getDecoder().decode(botInfo.identitySecret);

        int n2 = 8;
        if (tag != null) {
            if (tag.length() > 32) {
                n2 = 8 + 32;
            }
            else {
                n2 = 8 + tag.length();
            }
        }
        byte[] array = new byte[n2];
        int n3 = 8;
        while (true) {
            int n4 = n3 - 1;
            if (n3 <= 0) {
                break;
            }
            array[n4] = (byte)time;
            time >>= 8;
            n3 = n4;
        }
        if (tag != null) {

            byte[] copyArray = Arrays.copyOfRange(tag.getBytes(StandardCharsets.UTF_8), 0, n2-8);
            for(int i = 0; i < copyArray.length; i++) {
                array[i+8] = copyArray[i];
            }
        }

        try {

            byte[] hashedData = HmacSHA1Handler.calculateHMACSHA1(array,decode);

            String encodedData = Base64.getEncoder().encodeToString(hashedData);

            String hash = URLEncoder.encode(encodedData, StandardCharsets.UTF_8);
            return hash;
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

}