package com.example.demo.service;
import com.example.demo.Bot.BotManager;
import com.example.demo.models.entity.Skins;
import com.example.demo.models.entity.TradeOffer;
import com.example.demo.models.entity.UserProfile;
import com.example.demo.models.repository.TradeOfferRepository;
import com.example.demo.models.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalTime;
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
            activeTradeOffers = tradeOfferRepository.findAllByStatus(2);
            if (activeTradeOffers.size()>0)
            {
                for(int i =0;i < activeTradeOffers.size();i++)
                {
                    int status = botManager.requestTradeOfferStatus(activeTradeOffers.get(i).getId());
                    System.out.println(activeTradeOffers.get(i).getId()+":"+status);
                    if (status==3)
                    {
                        activeTradeOffers.get(i).setStatus(3);
                        tradeOfferRepository.save(activeTradeOffers.get(i));

                    }
                    if (status==7)
                    {
                        activeTradeOffers.get(i).setStatus(7);
                        tradeOfferRepository.save(activeTradeOffers.get(i));
                    }
                    currentTime = LocalTime.now();
                    Duration timeElapsed = Duration.between(activeTradeOffers.get(i).getCreateTime(), currentTime);
                    System.out.println(timeElapsed.toSeconds());
                    if (timeElapsed.toSeconds()>600)
                    {
                        if(botManager.cancelTradeOffer(activeTradeOffers.get(i).getId()))
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

    public void startTradeService()
    {
        botManager.initializeBot();
    }

    public String sendTradeOffer(List<Skins> botSkins, List<Skins> userSkins, UserProfile user) {

        String tradeOfferID= botManager.sendTradeOffer(botSkins,userSkins,user);
        return tradeOfferID;
    }


}
