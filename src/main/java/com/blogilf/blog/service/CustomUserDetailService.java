package com.blogilf.blog.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.blogilf.blog.model.User;
import com.blogilf.blog.model.UserPrincipal;
import com.blogilf.blog.repository.UserRepository;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    CustomUserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            System.out.println("user not found : " + username);
            throw new UsernameNotFoundException("user not found : " + username);
        }

        return new UserPrincipal(user.get());
    }
}