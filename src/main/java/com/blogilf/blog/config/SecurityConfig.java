package com.blogilf.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.blogilf.blog.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService customUserDetailService;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
            .csrf(csrf -> csrf.disable())
            
            .authorizeHttpRequests(request -> request
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated() 
            )

            .formLogin(Customizer.withDefaults())
            
            .httpBasic(Customizer.withDefaults())
            
            // for enabling h2-console
            .headers(headers -> headers.frameOptions().sameOrigin());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(5));
        provider.setUserDetailsService(customUserDetailService);
        return provider;
    }
}
