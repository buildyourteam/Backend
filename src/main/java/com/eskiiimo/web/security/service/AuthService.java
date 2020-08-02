package com.eskiiimo.web.security.service;

import com.eskiiimo.web.security.request.SignInRequest;
import com.eskiiimo.web.security.request.SignUpRequest;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.security.exception.CSigninFailedException;
import com.eskiiimo.web.security.exception.CUserNotFoundException;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.enumtype.UserState;
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

    public boolean signup(SignUpRequest signupRequest) {
        userRepository.save(User.builder()
                .userId(signupRequest.getUserId())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .userName(signupRequest.getName())
                .userEmail(signupRequest.getUserEmail())
                .activate(UserActivate.REGULAR)
                .state(UserState.FREE)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return true;
    }

    @Transactional(readOnly = true)
    public User signin(SignInRequest signInRequest) {
        User user = userRepository.findByUserIdAndActivate(signInRequest.getUserId(),UserActivate.REGULAR)
                .orElseThrow(() -> new CUserNotFoundException());
        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword()))
            throw new CSigninFailedException();
        return user;
    }

    @Transactional(readOnly = true)
    public boolean idCheck(String checkId) {
        Optional<User> optionalUser = userRepository.findByUserIdAndActivate(checkId,UserActivate.REGULAR);
        if (optionalUser.isEmpty())
            return true;
        else
            return false;
    }
}
