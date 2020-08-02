package com.eskiiimo.web.security.service;

import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.security.exception.CUserNotFoundException;
import com.eskiiimo.web.user.enumtype.UserActivate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userPk) {
        return userRepository.findByUserIdAndActivate(userPk, UserActivate.REGULAR)
                .orElseThrow(CUserNotFoundException::new);
    }
}
