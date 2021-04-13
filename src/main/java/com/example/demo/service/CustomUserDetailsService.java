package com.example.demo.service;

import com.example.demo.models.entity.Users;
import com.example.demo.models.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken> {
    private UsersRepository usersRepository;

    @Autowired
    public void setSpringUserRepository(UsersRepository UsersRepository) {
        this.usersRepository = UsersRepository;
    }

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        Users users;
        try {
            Optional<Users> usersOptional = usersRepository.findById(token.getName());
            if (usersOptional.isPresent()) users = usersOptional.get();
            else users = null;
            if (users != null) return users;
            throw new UsernameNotFoundException("Users is not found");
        } catch (RuntimeException e) {
            users = new Users();
            users.setId(token.getName());
            return usersRepository.save(users);
        }
    }
}