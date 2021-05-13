package com.example.demo.service;

import com.example.demo.Bot.BotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TradeService {

    @Autowired
    BotManager botManager = new BotManager();

    public void hello () throws IOException {
        botManager.main();
    }
}
