package com.example.demo.models.repository;

import com.example.demo.models.entity.BotDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotDetailsRepository extends JpaRepository<BotDetails,String> {
    BotDetails findBotDetailsBySteamLogin(String steamLogin);
}
