package com.example.demo.Bot.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHandler {

    public static List<String> getFirstMatch(String input, String regex){

        List<String> matchList = new ArrayList<>();
        try{
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(input);

            if(matcher.find()) {
                matchList.add(matcher.group(0));
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    matchList.add(matcher.group(i));
                }
            }
            return matchList;

        }catch(Exception err){
            System.out.println("There was a problem finding a match in regex");
            System.out.println(err.getMessage());
            err.printStackTrace();
            return null;
        }
    }

    public static List<List<String>> getAllMatches(String input, String regex) {
        List<List<String>> matchList = new ArrayList<>();
        List<String> match = new ArrayList<>();
        try{
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(input);

            while (matcher.find()) {
                match.add(matcher.group(0));
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    match.add(matcher.group(i));
                }
                matchList.add(match);
                match = new ArrayList<>();
            }

            return matchList;
        }catch(Exception err){
            System.out.println("There was a problem finding a match in regex");
            System.out.println(err.getMessage());
            err.printStackTrace();
            return new ArrayList<>();
        }
    }
}