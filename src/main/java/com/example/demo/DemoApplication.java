package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        TradeBot tb = new TradeBot();

        SpringApplication.run(DemoApplication.class, args);
        tb.run();
    }




}
