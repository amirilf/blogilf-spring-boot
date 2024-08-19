package com.blogilf.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.blogilf.blog.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public final static int encoderStrength = 5;

    private final JwtFilter jwtFilter;

    SecurityConfig(JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }
    
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
        
            .authorizeHttpRequests(request -> request
                .requestMatchers("/h2-console/**","/users/login","/users/register","/search").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated() 
            )
        
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(form -> form.disable())

            .httpBasic(Customizer.withDefaults())
            
            // jwt filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

            // for enabling h2-console
            .headers(headers -> headers.frameOptions().sameOrigin());
            
        return http.build();
    }
}
