package com.example.demo.Bot.Utils;

import com.example.demo.utls.EndPoints;
import com.example.demo.utls.HTTPClientGame;
import com.example.demo.utls.MyJsonParser;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class TimeAligner {

    private static boolean aligned = false;
    private static Long timeDifference = 0L;

    public static long getSteamTime(HttpClient client) throws IOException, InterruptedException {
        if (!TimeAligner.aligned) {
            TimeAligner.alignTime(client);
        }
        return TimeStampHandler.getCurrentTimeStamp() + timeDifference;
    }

    private static void alignTime(HttpClient client) throws IOException, InterruptedException {
        long currentTime = TimeStampHandler.getCurrentTimeStamp();

        HttpRequest request = HTTPClientGame.build(EndPoints.TWO_FACTOR_TIME_QUERY,
                new HashMap<String, String>(),
                HTTPClientGame.RequestType.POST,
                "steamid=0"
        );
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject jsonResponse = MyJsonParser.parseTimeAligner(response.body().toString());
        Long serverTime = jsonResponse.get("server_time").getAsLong();
        timeDifference = serverTime - currentTime;
        aligned = true;
    }
}