package com.eskiiimo.api.auth;

import com.eskiiimo.api.auth.exception.CUserNotFoundException;
import com.eskiiimo.api.user.UserRepository;
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
