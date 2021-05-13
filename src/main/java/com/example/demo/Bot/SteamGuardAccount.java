package com.example.demo.Bot;

import com.example.demo.Bot.Utils.HmacSHA1_Handler;
import com.example.demo.Bot.Utils.TimeAligner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

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

        byte[] hashedData = HmacSHA1_Handler.calculateHMACSHA1(timeArray, sharedSecretArray);

        byte[] codeArray = new byte[5];
        try {
            byte b = (byte)(hashedData[19] & 0xF);
            int codePoint = (hashedData[b] & 0x7F) << 24 | (hashedData[b + 1] & 0xFF) << 16 | (hashedData[b + 2] & 0xFF) << 8 | (hashedData[b + 3] & 0xFF);

            for (int i = 0; i < 5; ++i) {
                codeArray[i] = steamGuardCodeTranslations[codePoint % steamGuardCodeTranslations.length];
                codePoint /= steamGuardCodeTranslations.length;
            }
        }
        catch (Exception e) { //Not the best catcher but should work.
            return null;
        }
        return new String(codeArray, StandardCharsets.UTF_8);
    }


}