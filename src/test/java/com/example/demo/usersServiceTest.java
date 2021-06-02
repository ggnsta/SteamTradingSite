package com.example.demo;

import com.example.demo.models.entity.UserProfile;
import com.example.demo.models.repository.UserProfileRepository;
import com.example.demo.service.UsersService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class usersServiceTest {

    @Autowired
    UsersService usersService;
    @MockBean
    private UserProfileRepository userProfileRepository;

    @Test
    public void setTradeTokenAndParnerID()
    {
        UserProfile userProfile = new UserProfile();
        usersService.setTradeTokenAndPartnerID("https://steamcommunity.com/tradeoffer/new/?partner=441176099&token=QQ0h-VDw", userProfile);
        Assert.assertEquals("441176099",userProfile.getPartnerID());
        Assert.assertEquals("QQ0h-VDw", userProfile.getTradeToken());
    }

    @Test
    public void updateSteamInfo()
    {
        UserProfile userProfile = new UserProfile();
        userProfile.setId("76561198401441827");
        userProfileRepository.save(userProfile);
        usersService.updateSteamInfo(userProfile);
        Assert.assertEquals("Sam", userProfile.getName());
    }
    @Test
    public  void findSteamInfo()
    {
        UserProfile userProfile = new UserProfile();
        userProfile.setId("76561198401441827");
        userProfileRepository.save(userProfile);
        usersService.findSteamInfo(userProfile);
        Assert.assertEquals("Sam", userProfile.getName());
    }
}
