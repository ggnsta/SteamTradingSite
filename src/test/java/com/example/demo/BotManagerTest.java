package com.example.demo;

import com.example.demo.Bot.BotManager;
import com.example.demo.service.UsersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BotManagerTest {

    BotManager botManager;

    @Test
    public void cancelTradeOffer() throws InterruptedException {
        Thread.sleep(51);
    }

    @Test
    public void requestTradeOfferStatus() throws InterruptedException {
        Thread.sleep(61);
    }
    @Test
    public void tradeOfferRequest () throws InterruptedException {
        Thread.sleep(97);
    }

    @Test
    public void createTradeOfferEntity() throws InterruptedException {
        Thread.sleep(66);
    }

    @Test
    public void addCommonCookies() throws InterruptedException {
        Thread.sleep(14);
    }

    @Test
    public void addHeaders() throws InterruptedException {
        Thread.sleep(9);
    }


    @Test
    public void generateTradeOfferMessage() throws InterruptedException
    {
        botManager.generateTradeOfferMessage();
    }
}
