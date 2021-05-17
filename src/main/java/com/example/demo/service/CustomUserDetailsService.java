package com.example.demo.service;

import com.example.demo.models.entity.UserProfile;
import com.example.demo.models.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken> {
    @Autowired
    private UserProfileRepository userProfileRepository;


    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        UserProfile users;
        try {
            Optional<UserProfile> usersOptional = userProfileRepository.findById(token.getName().substring(37));
            if (usersOptional.isPresent()) users = usersOptional.get();
            else users = null;
            if (users != null) return users;
            throw new UsernameNotFoundException("Users is not found");
        } catch (RuntimeException e) {
            users = new UserProfile();
            users.setId(token.getName().substring(37));
            return userProfileRepository.save(users);
        }
    }
}