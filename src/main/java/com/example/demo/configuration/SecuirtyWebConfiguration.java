package com.example.demo.configuration;
import com.example.demo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.openid.OpenIDAuthenticationToken;

@EnableWebSecurity
public class SecuirtyWebConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/**").permitAll()
                .and()
                .openidLogin()
                .loginPage("/profile-info").permitAll()
                .authenticationUserDetailsService(authenticationUserDetailsService())
                .failureUrl("/?fail")
                .and()
                .csrf().disable();
    }


    @Bean
    public AuthenticationUserDetailsService<OpenIDAuthenticationToken> authenticationUserDetailsService() {
        return new CustomUserDetailsService();
    }
}
