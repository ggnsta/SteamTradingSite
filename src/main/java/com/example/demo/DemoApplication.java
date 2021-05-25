package com.example.demo;

import com.example.demo.Bot.BotInfo;
import com.example.demo.Bot.BotLogin;
import com.example.demo.Bot.BotManager;
import com.example.demo.models.entity.BotDetails;
import com.example.demo.models.repository.BotDetailsRepository;
import com.example.demo.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
public class DemoApplication {


    public static void main(String[] args) throws Exception {

        SpringApplication.run(DemoApplication.class, args);



    }



}
