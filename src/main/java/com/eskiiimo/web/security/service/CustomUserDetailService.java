package com.eskiiimo.web.security.service;

import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.security.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String userPk) {
        return userRepository.findByUserId(userPk).orElseThrow(CUserNotFoundException::new);
    }
}
