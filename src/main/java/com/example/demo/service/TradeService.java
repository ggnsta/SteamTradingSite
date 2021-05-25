package com.example.demo.service;

import com.example.demo.Bot.BotInfo;
import com.example.demo.Bot.BotManager;
import com.example.demo.models.entity.BotDetails;
import com.example.demo.models.entity.Skins;
import com.example.demo.models.entity.TradeOffer;
import com.example.demo.models.entity.UserProfile;
import com.example.demo.models.repository.BotDetailsRepository;
import com.example.demo.models.repository.TradeOfferRepository;
import com.example.demo.models.repository.UserProfileRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TradeService implements Runnable{

    @Autowired
    BotManager botManager = new BotManager();
    @Autowired
    private TradeOfferRepository tradeOfferRepository;
    private List<TradeOffer> activeTradeOffers;
    @Autowired
    private UserProfileRepository userProfileRepository;



    @Override
    public void run()
    {
        LocalTime currentTime;
        try {
            activeTradeOffers = tradeOfferRepository.findAllByStatus(2); //ищем в бд, офферы со статусом =2 (отправлен)
            if (activeTradeOffers.size()>0)
            {
                for(int i =0;i < activeTradeOffers.size();i++)
                {
                    int status = botManager.requestTradeOfferStatus(activeTradeOffers.get(i).getId());// запрашиваем у steam текущий статус этого офера
                    System.out.println(activeTradeOffers.get(i).getId()+":"+status);
                    if (status==3) // принят пользователем
                    {
                        activeTradeOffers.get(i).setStatus(3);
                        tradeOfferRepository.save(activeTradeOffers.get(i));
                        // TO-DO изменить баланс пользвоателя
                    }
                    if (status==7) // обмен отклонен
                    {
                        activeTradeOffers.get(i).setStatus(7);
                        tradeOfferRepository.save(activeTradeOffers.get(i));
                    }
                    currentTime = LocalTime.now();
                    Duration timeElapsed = Duration.between(activeTradeOffers.get(i).getCreateTime(), currentTime);
                    System.out.println(timeElapsed.toSeconds());
                    if (timeElapsed.toSeconds()>600) // если статус обмена не изменяется больше 10 минут после создания офера, т.е. пользователь его игнорирует, то отменяем обмен
                    {
                        if(botManager.cancelTradeOffer(activeTradeOffers.get(i).getId()))//отменяем обмен
                        {
                            activeTradeOffers.get(i).setStatus(7);
                            tradeOfferRepository.save(activeTradeOffers.get(i));
                        }
                    }
                }
            }
            else System.out.println(Thread.currentThread().getName());
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @PostConstruct
    public void startTradeService()
    {
        botManager.initializeBot();
    }

    public void sendTradeOffer(List<Skins> botSkins, List<Skins> userSkins, UserProfile user) {

        botManager.sendTradeOffer(botSkins,userSkins,user);

    }


}
