package com.eskiiimo.web.security.service;

import com.eskiiimo.repository.security.dto.SignInDto;
import com.eskiiimo.repository.security.dto.SignUpDto;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.security.exception.CSigninFailedException;
import com.eskiiimo.web.security.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public boolean signup(SignUpDto signupDto) {
        userRepository.save(User.builder()
                .userId(signupDto.getUserId())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .userName(signupDto.getName())
                .userEmail(signupDto.getUserEmail())
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return true;
    }

    @Transactional(readOnly = true)
    public User signin(SignInDto signInDto) {
        Optional<User> optionalUser = userRepository.findByUserId(signInDto.getUserId());
        if(optionalUser.isEmpty())
            throw new CUserNotFoundException();
        User user = optionalUser.get();
        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword()))
            throw new CSigninFailedException();
        return user;
    }

    @Transactional(readOnly = true)
    public boolean idCheck(String checkId) {
        Optional<User> optionalUser = userRepository.findByUserId(checkId);
        if(optionalUser.isEmpty())
            return true;
        else
            return false;
    }
}
