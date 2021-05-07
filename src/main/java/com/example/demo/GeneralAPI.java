package com.example.demo;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

public class GeneralAPI {

    protected static final File proxiesFile = new File("proxies.txt");
    public static String currentLog = "...";
    protected static final List<String> proxies = new ArrayList<>();
    protected static int index = 0;

    public static File getProxiesFile() {
        return proxiesFile;
    }

    public static List<String> getProxies() {
        return proxies;
    }

    public static void loadProxies() {
        try (BufferedReader br = new BufferedReader(new FileReader(getProxiesFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                proxies.add(line);
            }
            GeneralAPI.currentLog = "[INFO] Proxies loaded.";
            GeneralAPI.currentLog = "[INFO] " + getProxies().size() + " proxies available.";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Proxy choiceProxy() {
        String proxyStr = getProxies().get(index);
        index++;
        String[] split = proxyStr.split(":");
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(split[0], Integer.parseInt(split[1])));
    }

    public static boolean proxiesEnabled() {
        return getProxies().size() != 0;
    }
}