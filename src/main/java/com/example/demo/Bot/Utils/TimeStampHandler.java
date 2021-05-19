package com.example.demo.Bot.Utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampHandler {


    /**
     * Get current system time in Epoch or Unix Timestamp
     * @return Current Time
     */
    public static Long getCurrentTimeStamp(){
        Date dt = new Date();
        return dt.getTime()/1000; //This is the format needed by steam Api.
    }

    /**
     * Get Epoch timestamp from a string time value
     * @param time_String Example Formate : "2020-01-20T00:00:00.000-0000"
     * @return
     */
    public static String getUint32TimeStamp(String time_String) throws Exception{
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date dt = simpleDateFormat.parse(time_String);
            long epoch = dt.getTime();
            return Integer.toString((int)(epoch/1000));
        }catch(Exception err) {
            throw new Exception("Could not generate timestamp. Check format of Date string");
        }
    }
}